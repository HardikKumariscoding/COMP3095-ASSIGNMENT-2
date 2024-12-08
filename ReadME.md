RoomService: Manages room resources, such as room availability, capacity, and features.
• BookingService: Handles room booking requests and manages booking information.
• UserService: Manages user profiles for students, staff, and faculty.
• EventService: Manages events linked to room bookings.
• ApprovalService: Allows staff to approve or reject event requests based on college policy.




Relational Data Modeling:
• Seed sample data into your databases where/when deemed necessary/useful.
• Establish relationships between your entities:
1. RoomService:
   • Use PostgreSQL / JPA for data storage.
   • Manage room resources with attributes like roomName, capacity, features (e.g., projector, whiteboard), and
   availability. Of course, the final design is up to each individual group.
   • Provide endpoints to check room availability for bookings.
2. BookingService:
   • Use MongoDB for data storage.
   • Handle room booking requests. Each booking should include userId, roomId, startTime, endTime, and
   purpose. Of course, the final design is up to each individual group.
   • Prevent double-booking of rooms using appropriate validation logic.
3. UserService:
   • Use PostgreSQL / JPA to store user information.
   • Manage user profiles for students, staff, and faculty with attributes like name, email, role, and userType
   (student, staff, faculty). Of course, the final design is up to each individual group.
   • Implement role-based access (e.g., only staff can approve events etc…).
4. EventService:
   • Use MongoDB to manage events.
   • Create events linked to room bookings with attributes such as eventName, organizerId, eventType, and
   expectedAttendees.
5. ApprovalService:
   • Use MongoDB or PostgreSQL to store approval.
   • Allow staff to review and approve/reject event requests. Link event approvals to the user role.