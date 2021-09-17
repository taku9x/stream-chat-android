## Common changes for all artifacts
### 🐞 Fixed

### ⬆️ Improved

### ✅ Added

### ⚠️ Changed

### ❌ Removed


## stream-chat-android
### 🐞 Fixed

### ⬆️ Improved

### ✅ Added

### ⚠️ Changed

### ❌ Removed


## stream-chat-android-client
### 🐞 Fixed
- Fixed sending messages using `ChatClient::sendMessage` without explicitly specifying the sender user id.

### ⬆️ Improved
- It is now possible to add a interceptor for API calls of Stream API. You can add it in the Build of ChatClient:
```
val client = ChatClient.Builder(apiKey, context)
    .loggerHandler(FirebaseLogger)
    .notifications(notificationHandler)
    .logLevel(logLevel)
    .apiLoggerInterceptor(HttpLoggingInterceptor())
    .build()
```

### ✅ Added

### ⚠️ Changed
- Now it is possible to hard delete messages. Insert a flag `hard = true` in the `ChatClient.deleteMessage` and it will be deleted in the backend. **This action can't be undone!**

### ❌ Removed

## stream-chat-android-offline
### 🐞 Fixed

### ⬆️ Improved

### ✅ Added

### ⚠️ Changed

### ❌ Removed


## stream-chat-android-ui-common
### 🐞 Fixed

### ⬆️ Improved

### ✅ Added

### ⚠️ Changed

### ❌ Removed


## stream-chat-android-ui-components
### 🐞 Fixed

### ⬆️ Improved

### ✅ Added

### ⚠️ Changed

### ❌ Removed


## stream-chat-android-compose
### 🐞 Fixed

### ⬆️ Improved

### ✅ Added

### ⚠️ Changed

### ❌ Removed


## stream-chat-android-pushprovider-firebase
### 🐞 Fixed

### ⬆️ Improved

### ✅ Added

### ⚠️ Changed

### ❌ Removed
