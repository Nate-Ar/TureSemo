## Getting Started

- **Install Dependencies:**

  ```sh
  pip install flask flask_sqlalchemy flask_bcrypt flask_login flask_wtf wtforms

- **Create .env file:**
  ```sh
  touch .env

- **Enter Python REPL**
  ```sh
  python

- **Generate a Secret Key in Python REPL and add it to .env:**
  ```python
  import secrets
  print(secrets.token_hex(32))

- **Add the generated key to your .env file:**
  ```env
  FLASK_SECRET_KEY=your_generated_secret_key

- **Run the app.py file:**
  ```sh
  python app.py

## Expected File Tree Layout
  ```sh
  /backend
│── /templates
│   ├── home.html
│   ├── register.html
│   ├── login.html
│── app.py
│── .env
│── README.md
