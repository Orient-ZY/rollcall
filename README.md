# rollcall

A application for rollcall.

# How to use it

## Create database

![table](https://user-images.githubusercontent.com/26559141/46590296-c0c09180-cae4-11e8-8b3d-8b4bcb925064.png)

- modified the database url in `config.properties`->`url`

- create table named `reg` and `record`. 
**The table should be named `reg` store the roster of the class, and there are 3 fields named: `uid`, `sid` and `name` means userID, studentID and studentName separately. The table named `record` is order to record those students who are deficient, there are also three fields names: `time`, `sid` and `name` means recordTime, studentID and studentName separately.**

## Import roster to database

- Firstly transform the roster to txt file, the file content format like this:
```
111111111111,张三
222222222222,李四
……
```

- Open the app, input the password: **"input"** and then the import page appear.

![homepage](https://user-images.githubusercontent.com/26559141/46590514-704a3380-cae6-11e8-9058-19b5857b0c8f.png)

![import page](https://user-images.githubusercontent.com/26559141/46590514-704a3380-cae6-11e8-9058-19b5857b0c8f.png)

- Move the roster txt file to the path that same to the app. Input the roster txt file into the textfield and then press the button `导 入`. The success tip will appear if there is no error.

## The roll-call page

- Input the password: "call" in the homepage, then the roll-call page will appear.

![roll-call page](https://user-images.githubusercontent.com/26559141/46591290-48f66500-caec-11e8-890c-2d612baae0be.png)

- This page has 4 main components: startBtn, stopBtn, recordBtn and displayLabel. Press "开 始", the timer then start, and the displayLabel will show the studentID and studentName one by one. Press "停 止", the timer will stop and the displayLabel will only show one student info. Press "记 录", the student info in the displayLabel will record to database.

## The result page

This page would appear while inputing the password "result". It shows the info who was recorded.
