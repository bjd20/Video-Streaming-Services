cd eureka-server
./gradlew clean build -x test
cd..

cd api-gateway
./gradlew clean build -x test
cd ..

cd account-service
./gradlew clean build -x test
cd ..

cd video-service
./gradlew clean build -x test
cd ..


