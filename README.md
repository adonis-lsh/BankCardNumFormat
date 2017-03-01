##提交到maven仓库遇到的坑
- 第一次用了比较简单的novoda库,看着很简单,但是遇到了不怎么怎么解决的坑![](http://i.imgur.com/le91CIw.png)
- 因为gradle的时候没有生成相应的文件,所以上传的时候缺少pom文件,在上传到jCenter的过程中就会报错![](http://i.imgur.com/3LNPXU4.png)
###解决办法:
1. 用Bintray和jCenter库,不过要自己去maven上面创建一个包,期间还会遇到一些问题,不要用Android Stdio下面的命令框去提交问题,我们用windows的很窗口就可以解决一些问题.
2. 也可以使用novoda库加入如下的代码:


   ```task sourcesJar(type: Jar) {
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