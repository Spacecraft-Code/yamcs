FROM maven:latest

WORKDIR /yamcs

RUN git clone https://github.com/yamcs/quickstart.git myproject

EXPOSE 8090

RUN cd ./myproject && mvn yamcs:run
