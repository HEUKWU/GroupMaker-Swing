# 그룹 생성기

## 개발 목표

이전에 (SpringBoot + MongoDB) 웹으로 제작했던 그룹 생성 프로그램을 Java의 Swing 라이브러리를 사용하여  
애플리케이션으로 제작해보려고 한다.

## 기술 스택

- Java17
- Swing Library

## 기능 요구사항

- 나눌 그룹 개수를 입력하고 그룹 생성 버튼을 클릭하면 저장된 이름 목록을 무작위로 섞어  
사용자가 입력한 그룹 개수를 기반으로 그룹을 생성한다.  
(올바르지 않은 개수 입력시 재입력을 요구하는 알림창을 생성한다.)
- 이름을 추가하거나 삭제할 수 있다.  
(올바르지 않은 이름 입력시 재입력을 요구하는 알림창을 생성한다.)