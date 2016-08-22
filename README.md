# lolx-backend

## to start locally from InteliJ

-Dspring.profiles.active=local

## to gen keys

To generate a key, use:

$ openssl genrsa -out private_key.pem 2048
Or, with a passphrase:

$ openssl genrsa -aes256 -out private_key.pem 2048
Or, using ssh-keygen (following your example):

$ ssh-keygen -t rsa -C "myEmail" -I X.509
I'll assume you saved the key as 'private_key.pem' Generate a public key in DER format:

$ openssl rsa -in private_key.pem -pubout -outform DER -out tst_public.der
A byte array containing the contents of the file will now be accepted by X509EncodedKeySpec.

If you want to load the private key, use OpenSSL to save an unencrypted copy of your private key (don't do this in an insecure environment):

$ openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -out private_key.der -nocrypt


You can then pass this file as a byte array to `PKCS8EncodedKeySpec`.