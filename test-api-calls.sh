#!/bin/bash

#set -e

echo "1️⃣ Registering user..."
curl -X POST http://localhost:8080/api/account/users \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "TestUser",
    "email": "user@test.com",
    "password": "myPassword@123"
  }'

echo -e "\n\n2️⃣ Logging in..."
curl -X POST http://localhost:8080/api/account/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "TestUser",
    "password": "myPassword@123"
  }'

echo -e "\n\n🔑 Paste JWT token and press ENTER:"
read -r JWT_TOKEN

if [ -z "$JWT_TOKEN" ]; then
  echo "❌ JWT token cannot be empty"
  exit 1
fi

echo -e "\n\n3️⃣ Creating video..."
curl -X POST http://localhost:8080/api/videos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "title": "New Test Video",
    "description": "Testing video is described",
    "videoUrl": "https://test-example.com/video.mp4",
    "duration": 120,
    "thumbnailUrl": "https://example.com/thumbnail.jpg"
  }'
