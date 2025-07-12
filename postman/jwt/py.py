import base64
import jwt
import datetime

# Zmienna secretKey zakodowana w Base64
secret_key = "Q4x6KvG9t6z6JjFnY5Lu8P4vZ4LgYw9dX2B5gX9aJkI="

# Dekodowanie secretKey (Base64 -> raw key)
decoded_key = base64.b64decode(secret_key)

# Dane dla nowego JWT
payload = {
    "sub": "unverified.employee@domain.pl",  # Email z poprzedniego tokena
    "role": "UNVERIFIED_EMPLOYEE",  # Rola z poprzedniego tokena
    "iat": datetime.datetime.utcnow(),  # Czas wydania
}

# Generowanie nowego tokena JWT
new_token = jwt.encode(payload, decoded_key, algorithm="HS256")
print(new_token)
