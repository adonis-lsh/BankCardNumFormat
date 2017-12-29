# 一个可以自动格式化银行卡号并且自动识别那个银行的EditText
### 先上图

这个是全部验证银行卡号以后的展示图

![](http://i.imgur.com/YxoEW4y.gif)

这是设置不全验证以后的展示图


<img src="http://opgkgu3ek.bkt.clouddn.com/17-12-29/22684831.jpg" width="400px">

## 使用方法

在原来的基础上加上了setFullVerify(boolean isFullVerify),不设置的话默认是银行卡输入全了,验证银行卡号正确再去获取所属银行,设置为false以后,再输入银行卡前6位的时候就会去判断是哪个银行,银行卡输入全以后会再去判断时候是银行卡号的正确格式,然后再返回是那一个银行.
错误码说明:

* FAILCODE :没有查询到是那一个银行
* CARDNUMERROR : 银行卡校验不正确


```
BankNumEditText bankNumEditText = (BankNumEditText) findViewById(R.id.bankCardNum);
        final EditText editText = (EditText) findViewById(R.id.bankName);

        bankNumEditText
                .setFullVerify(false)
                .setBankNameListener(new BankNumEditText.BankNameListener() {
                    @Override
                    public void success(String name) {
                        editText.setText(name);
                    }

                    @Override
                    public void failure(int failCode, String failmsg) {
                        editText.setText(failCode+failmsg);
                    }
                });
```


```java
		 compile 'com.lsh.bankcardnum:banknumformatlibrary:1.0.1'
```
## 提交到maven仓库遇到的坑
- 第一次用了比较简单的novoda库,看着很简单,但是遇到了不怎么怎么解决的坑![](http://i.imgur.com/le91CIw.png)
- 因为gradle的时候没有生成相应的文件,所以上传的时候缺少pom文件,在上传到jCenter的过程中就会报错![](http://i.imgur.com/3LNPXU4.png)

### 解决办法:
1. 用Bintray和jCenter库,不过要自己去maven上面创建一个包,期间还会遇到一些问题,不要用Android Stdio下面的命令框去提交问题,我们用windows的很窗口就可以解决一些问题.
2. 也可以使用novoda库加入如下的代码:


```java
	    task sourcesJar(type: Jar) {
                from android.sourceSets.main.java.srcDirs
                classifier = 'sources'
            }
            task javadoc(type: Javadoc) {
                source = android.sourceSets.main.java.srcDirs
                classpath += project.files(android.getBootClasspath().join(File.pathSeparator))
            }
            task javadocJar(type: Jar, dependsOn: javadoc) {
                classifier = 'javadoc'
                from javadoc.destinationDir
            }
            artifacts {
                archives javadocJar
                archives sourcesJar
            }
```




