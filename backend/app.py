from flask import Flask, render_template, redirect, url_for, flash
from flask_sqlalchemy import SQLAlchemy
from flask_bcrypt import Bcrypt
from flask_login import LoginManager, UserMixin, login_user, logout_user, login_required
from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, SubmitField
from wtforms.validators import DataRequired, Length, EqualTo
import os

# Initialize Flask app
app = Flask(__name__)
app.config['SECRET_KEY'] = os.getenv('FLASK_SECRET_KEY', 'your_secret_key_here')  # Ensure you set a proper secret key
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///database.db'  # SQLite database URI
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False  # Disable modification tracking (optional)

# Initialize database, bcrypt, and login manager
db = SQLAlchemy(app)
bcrypt = Bcrypt(app)
login_manager = LoginManager(app)
login_manager.login_view = 'login'

# User model
class User(db.Model, UserMixin):
    id = db.Column(db.Integer, primary_key=True)
    first_name = db.Column(db.String(100), nullable=False)
    last_name = db.Column(db.String(100), nullable=False)
    semo_key = db.Column(db.String(50), unique=True, nullable=False)
    email = db.Column(db.String(150), unique=True, nullable=False)
    username = db.Column(db.String(100), unique=True, nullable=False)
    password = db.Column(db.String(255), nullable=False)

    def __repr__(self):
        return f"User('{self.username}', '{self.email}', '{self.semo_key}')"

# User loader callback for Flask-Login
@login_manager.user_loader
def load_user(user_id):
    return User.query.get(int(user_id))

# Registration Form (using Flask-WTF)
class RegistrationForm(FlaskForm):
    first_name = StringField('First Name', validators=[DataRequired(), Length(min=2, max=100)])
    last_name = StringField('Last Name', validators=[DataRequired(), Length(min=2, max=100)])
    semo_key = StringField('Southeast Key', validators=[DataRequired(), Length(min=4, max=50)])
    email = StringField('Email')  # No validation for now
    username = StringField('Username', validators=[DataRequired(), Length(min=4, max=100)])
    password = PasswordField('Password', validators=[DataRequired(), Length(min=6)])
    confirm_password = PasswordField('Confirm Password', validators=[DataRequired(), EqualTo('password')])
    submit = SubmitField('Register')

# Login Form (using Flask-WTF)
class LoginForm(FlaskForm):
    username = StringField('Username', validators=[DataRequired()])
    password = PasswordField('Password', validators=[DataRequired()])
    submit = SubmitField('Login')

# Routes
@app.route('/')
def home():
    return render_template('home.html')  # Render the home page

@app.route('/register', methods=['GET', 'POST'])
def register():
    form = RegistrationForm()  # Create a RegistrationForm instance
    if form.validate_on_submit():  # If the form is submitted and valid
        hashed_password = bcrypt.generate_password_hash(form.password.data).decode('utf-8')  # Hash the password
        user = User(
            first_name=form.first_name.data,
            last_name=form.last_name.data,
            semo_key=form.semo_key.data,
            email=form.email.data,
            username=form.username.data,
            password=hashed_password
        )
        db.session.add(user)  # Add the user to the session
        db.session.commit()  # Commit the changes to the database
        flash('Account created successfully! You can now log in.', 'success')  # Flash success message
        return redirect(url_for('login'))  # Redirect to the login page
    return render_template('register.html', form=form)  # Render the registration page with the form

@app.route('/login', methods=['GET', 'POST'])
def login():
    form = LoginForm()  # Create a LoginForm instance
    if form.validate_on_submit():  # If the form is submitted and valid
        user = User.query.filter_by(username=form.username.data).first()  # Query the user by username
        if user and bcrypt.check_password_hash(user.password, form.password.data):  # Correctly check password hash
            login_user(user)  # Log the user in
            flash('Login successful!', 'success')  # Flash success message
            return redirect(url_for('home'))  # Redirect to the home page
        else:
            flash('Login failed. Check your username and password.', 'danger')  # Flash error message
    return render_template('login.html', form=form)  # Render the login page with the form

@app.route('/logout')
@login_required  # Protect this route with login_required
def logout():
    logout_user()  # Log the user out
    flash('You have been logged out.', 'info')  # Flash info message
    return redirect(url_for('login'))  # Redirect to the login page

# Main entry point
if __name__ == '__main__':
    with app.app_context():
        db.create_all()  # Ensure the database tables are created before running
    app.run(debug=True)  # Run the Flask app with debugging enabled
