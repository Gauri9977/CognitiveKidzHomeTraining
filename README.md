**Commands for cloning repo:**
git clone git@github.com:CognoAppProject/CognitiveKidz.git

**Commands to add ssh key:**

1)create: ssh-keygen -t rsa -b 4096 -C "cognitivekidzhometraining@gmail.com"

2)check: ls -l ~/.ssh/

3)edit cofig: nano ~/.ssh/config
   (editable window will open. Add following lines there and press Clt+X —> Y —>enter
    “Host github.com
    IdentityFile ~/.ssh/id_rsa_cogno
    User git”

   Id_rsa_cogno : ssh key file name )

4)check connection successful or not: ssh -T git@github.com
   Should respond with message like: Hi <your GitHub username>! You've successfully authenticated, but GitHub does not provide shell access.
