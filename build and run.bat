@echo off
if not exist class mkdir class
javac -d class GameManager.java
java -cp class GameManager
