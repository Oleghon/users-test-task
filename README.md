
# Users - java practical test assignment
Users - is a simple REST application what covered the practical test assignment. 
####
App contains controller and service for the user entity, validation, exception handling, 
custom annotation and test coverage. 
Every user keep some required (id, email, first name, last name birth date) and optional (address, phone number) fields.
HashMap is used as storage.

## REST end-points:
### 1. GET /users/all 
   - show all users 
### 2. GET /users/{id} 
   - show user with specific id
### 3. POST /users/ 
   - create new user
   - to create new user request body must pass validation 
### 4. PUT /users/{id} 
   - full update of user
   - to update user request body must pass validation
### 5. PATCH /users/{id} 
   - part update of user
### 6. POST /users/search 
   - show filtered users by specific date range
   - request body must contain JSON value with two dates: 
```
     {
     "from": "yyyy-mm-dd",
     "to": "yyyy-mm-dd"
     }
```
   - to get filtered users request body must pass validation

   
