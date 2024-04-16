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

### core sub-directory

All codes unrelated to business logic, these codes will be re-used in all bounded-contexts.

### edge sub-directory

In this application, it will include implementations of BFF controllers for wechat mini-program front-end.

### cqrs sub-directory

Codes of those bounded-contexts' inquire model. It includes inquire model for chain context, order context and product context.

## Blog articles

In order to understand the code better, I suggest you read the blog series following. I will update the code here with the update of those articles. <br/>
[《DDD实战：从需求到代码实现生鲜电商系统》](https://www.infoq.cn/u/beautautumn)
