# CmpE561-Assignment 2
-->I worked on macbook osx 10.10.3 and Eclipse luna 4.4.1

-->I added an executable jar file into the repository.

-->I also added the copy of the report.

-->The output file is created in the directory in output.txt file (not in the "metu_sabanci_cmpe_561/output.txt" because of the path splitter difference in windows and mac)

To run the executable jar on command line:

    java -jar CmpE561.jar
  
To run the code itself from command line:
  
    javac -d bin/ -cp src src/cmpe561project2/Main.java
  
    java -cp bin cmpe561project2.Main


======= This is the example of system i/o ======= 

Enter the path of training file.. 

    metu_sabanci_cmpe_561/train/turkish_metu_sabanci_train.conll

Enter the path of test file.. 

    metu_sabanci_cmpe_561/validation/turkish_metu_sabanci_val.conll

CPOSTAG or POSTAG

    POSTAG

Enter the path of file gold standard file

    metu_sabanci_cmpe_561/validation/turkish_metu_sabanci_val.conll


======= Output =======

    The accuracy of CPOSTAG is 0.7947368421052632

        ------------------Confusion Matrix---------------

        null Adj Adv Conj Det Dup Interj Noun Num Postp Pron Punc Ques Verb Zero 

        Adj 0 53 3 109 0 0 30 5 7 2 0 0 27 0 

        Adv 0 0 0 0 0 0 3 0 18 0 0 0 0 0 

        Conj 0 0 0 0 0 0 2 0 0 0 0 0 3 0 

        Det 0 0 0 0 0 0 0 0 0 5 0 0 0 0 

        Dup 0 0 0 0 0 0 0 0 0 0 0 0 0 0 

        Interj 0 1 0 0 0 0 0 0 0 0 0 0 0 0 

        Noun 126 45 0 0 0 0 0 9 1 2 0 0 244 0 

        Num 0 0 0 0 0 0 0 0 0 0 0 0 0 0 

        Postp 0 0 0 0 0 0 3 0 0 0 0 0 0 0 

        Pron 0 0 0 0 0 0 5 0 0 0 0 0 0 0 

        Punc 0 0 0 0 0 0 0 0 0 0 0 0 0 0 

        Ques 0 0 0 0 0 0 0 0 0 0 0 0 0 0 

        Verb 3 0 0 0 0 0 10 1 19 5 0 0 0 0 

        Zero 0 0 0 0 0 0 0 0 0 0 0 0 0 0


