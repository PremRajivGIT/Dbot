#Readme File

*To run this on your system you must have the following
* Discord Bot, Java 17 or the version which supports JDA and Gradle
* must import Lava player Repo and JDA repo using Gradle
* with prerequisites complete you can run the basic music bot with minor configuration like adding the token etc
* for AI features you must have LM studio installed for Chat feature then install any LLM in the studio and use server feature of LM studio to get the link and paste it in PromptChat code file
* For Music Gen you must have musicgen model locally installed for how to refer HuggingFace musicgen repo. then you can run the python script using Process Builder(make sure to add python script path in MakeMusic.java file).
* if done correctly and some specific tuning later you can run your own bot
  
2nd Method:

NOTE: Got Docker IMAGE for this Bot, AI Features remain wrinkled (trying to fix them) but music player functions fine!

* to run it install docker in your system.
* and run these commands in sequence:
* 1. docker pull premrajivdocker/dbot
* 2. docker run premrajivdocker/dbot
  
Another NOTE: make sure that dbot is in your server   
