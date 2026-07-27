#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

echo "1️⃣ Registering user..."
USER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/account/users \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "TestUser",
    "email": "user@test.com",
    "password": "myPassword@123"
  }')
USER_ID=$(echo "$USER_RESPONSE" | jq -r '.id')
echo "User created with ID: $USER_ID"

echo -e "\n\n2️⃣ Logging in..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/account/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "TestUser",
    "password": "myPassword@123"
  }')
JWT_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token')
echo "Logged in successfully. JWT token captured."

echo -e "\n\n3️⃣ Creating video..."
VIDEO_RESPONSE=$(curl -s -X POST http://localhost:8080/api/video/videos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "X-User-Id: $USER_ID" \
  -H "X-User-Name: TestUser" \
  -d '{
    "title": "New Test Video",
    "description": "Testing video is described",
    "videoUrl": "https://test-example.com/video.mp4",
    "duration": 120,
    "thumbnailUrl": "https://example.com/thumbnail.jpg"
  }')
VIDEO_ID=$(echo "$VIDEO_RESPONSE" | jq -r '.id')
echo "Video created with ID: $VIDEO_ID"

echo -e "\n\n4️⃣ Deleting video..."
curl -s -X DELETE http://localhost:8080/api/video/videos/"$VIDEO_ID" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "X-User-Id: $USER_ID" \
  -H "X-User-Name: TestUser"
echo "Video with ID: $VIDEO_ID deleted."

echo -e "\n\n5️⃣ Deleting user..."
curl -s -X DELETE http://localhost:8080/api/account/users/"$USER_ID" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "X-User-Id: $USER_ID" \
  -H "X-User-Name: TestUser"
echo "User with ID: $USER_ID deleted."

echo -e "\n\n✅ API calls test finished successfully."
