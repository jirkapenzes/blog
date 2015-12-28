FROM java:8
MAINTAINER Jirka Penzes <jirkapenzes@gmail.com>

WORKDIR /blog
COPY resources resources
COPY target/blog-0.1.0-SNAPSHOT-standalone.jar /blog/

CMD ["java", "-jar", "blog-0.1.0-SNAPSHOT-standalone.jar"]
