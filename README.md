# Introduction

This is a demo application for DDD software architecture methodology.

## You can

* Free distribute, use and modify these codes.
* Free include it in your business projects.

## You must

* Follow Apache 2.0 license: [https://www.apache.org/licenses/LICENSE-2.0.html](https://www.apache.org/licenses/LICENSE-2.0.html)

## Code structure

### foundation sub-directory

All codes of those bounded-contexts in foundation layer are placed in. 
In general, these bounded-context include implementations of generic domains and/or supporting domains.

### valueadded sub-directory

All codes of those bounded-contexts in valued-added layer are placed in.
In general, these bounded-context include implementations of core domains.

### edge sub-directory

In this application, it will include only implementations of BFF controllers for wechat mini-program front-end.