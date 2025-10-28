@echo off
if not exist class mkdir class
javac -d class scene\GameManager.java
java -cp class scene.GameManager
