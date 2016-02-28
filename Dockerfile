FROM java:8
MAINTAINER Jirka Penzes <jirkapenzes@gmail.com>

ENV VIRTUAL_HOST="blog.penzes.cz"

WORKDIR /blog
COPY resources resources
COPY target/blog-0.1.0-SNAPSHOT-standalone.jar /blog/

EXPOSE 8081

CMD ["java", "-jar", "blog-0.1.0-SNAPSHOT-standalone.jar"]
