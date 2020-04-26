chmod +x script.sh
git clone https://gitlab.cs.unh.edu/cs953-spring-2020/team07-submission03.git
cd team07-submission03


mvn clean install

wget "http://www.cs.unh.edu/~dietz/download/got.cbor"

java -jar target/project-1.0-SNAPSHOT-jar-with-dependencies.jar deaths-train.csv deaths-test.csv got.cbor Text/

