FROM flink:1.13.1-scala_2.12

RUN mkdir /cloud
COPY ./ /cloud
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list &&\
    echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list &&\
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add &&\
    apt update -y && apt upgrade -y && apt install sbt -y
RUN cd /cloud; sbt clean assembly
RUN mv /cloud/target/scala-2.12/lc_data_ingestion.jar /job.jar

CMD ["jobmanager"]