1. Project Proposal: A Communication App for Motorcyclists
 
Objective: The aim of this project is to develop an innovative communication app specifically designed for motorcyclists. The app will allow bikers to communicate seamlessly with each other and with their passengers during rides, enhancing safety, coordination, and overall riding experience.
	-
Key Elements of a Proposal:
Problem Statement:Motorcyclists often face difficulties in communicating with their fellow riders or passengers while on the move. Existing solutions, such as physical hand signals or expensive helmet-based communication systems, can be limiting, unreliable, or unaffordable for many riders. This lack of real-time, clear communication increases the risk of misunderstandings, leading to potential hazards on the road and a less enjoyable riding experience.

Proposed Solution:The app will provide an accessible and user-friendly communication platform for motorcyclists. Using Bluetooth or internet-based connectivity, the app will allow riders to:
Communicate with their passengers without the need for additional hardware.
Connect with other bikers in a riding group for real-time voice communication.
Use voice commands(optional) and a simple interface special to ensure safety during rides.(ride mode).

Target Audience: 
Motorcycle Enthusiasts: Individuals who frequently ride motorcycles, either alone or in groups, and need an efficient way to communicate on the road.
Riders with Passengers: Motorcyclists who carry passengers on their bikes and require easy communication for safety and comfort.
Riding Groups/Clubs: Organized groups of bikers who often ride together and benefit from coordinated communication.

Tools & Technologies: Android Studio(frontend), Firebase(backend)
Timeline & Milestones:
Timeline:
By week 6
Users can make accounts and connect with each other on the application.

By week 8
Users can call each other

By week 10
Users can locate each other on the minimap, and start calls




2. Requirements Gathering:
 
Functional Requirements: 

User Registration and Authentication:
The app must allow users to create accounts with email or phone number.
Users must be able to log in securely using a password.
Real-Time Communication:
The app must enable motorcyclists to start voice calls with passengers or other riders in real-time.
Users should be able to create or join group voice chats with multiple riders.
The app must support push-to-talk (PTT) functionality for voice communication.
Bluetooth Connectivity:
The app must support Bluetooth communication for real-time interaction with a passenger or other nearby bikers without relying on mobile data.
Notifications and Alerts:
The app should notify users of incoming calls, messages, or alerts from group members.
Offline Functionality(Optional):
The app should support basic communication features via Bluetooth when internet connectivity is unavailable.
Location Sharing:
The app must allow riders to share their live location with fellow riders in the group.






Non-Functional Requirements:

Performance:
The app must maintain a low-latency voice communication experience (less than 100ms delay) for a seamless user experience.
The app should consume minimal battery power and optimize resource usage to extend phone life during long rides.
Reliability:
The app must provide uninterrupted voice communication even at high speeds (up to 120 km/h) and under different weather conditions.
It must be robust enough to handle fluctuating network conditions, automatically reconnecting calls if the connection drops.
Usability:
The user interface (UI) must be simple, intuitive, and easily accessible while riding (e.g., large buttons for voice chats and navigation).
The app should be easy to operate with one hand or via voice commands for safety.
Security:
User data, including location and voice communication, must be encrypted to protect privacy.
The app must implement strong authentication and data protection measures (e.g., HTTPS, two-factor authentication).
Cross-Platform Compatibility:
The app should be available for both Android and iOS platforms, ensuring it works seamlessly on a range of devices.
Durability (in terms of usage):
The app should be resistant to sudden crashes or bugs and should be able to recover gracefully from interruptions (e.g., phone calls, battery saving mode).


Techniques for Gathering Requirements:
Use Cases/User Stories: 
Use Cases/User Stories:
User Story 1: As a biker, I want to initiate a voice chat with my passenger, so we can communicate without shouting while riding.
User Story 2: As a biker in a group ride, I want to be able to communicate hands-free with fellow riders, so I can keep my focus on the road.
User Story 3: As a passenger, I want to quickly connect to the rider through the app, so I can easily alert them of any issues without distractions.


Stakeholder Collaboration: 
Motorcyclists: Gather insights by conducting interviews, focus groups, or surveys with motorcyclists. Ask about their communication challenges on the road and what specific features they would want in a voice communication app. Focus on situations where immediate communication is important, such as alerting other riders to hazards, coordinating breaks, or simply socializing while riding.
Passengers: Engage with passengers to understand their perspective on communicating with riders. Find out what features they would value, such as the ability to speak clearly with the rider at high speeds.
Motorcycle Groups/Clubs: Talk to members of motorcycle clubs or riding groups to learn about the dynamics of group rides. Focus on how they currently communicate during rides and how an app could simplify voice communication among large groups without distracting from riding.
Technical Experts: Talk to the networks professor and IIoTCA lab professor. 👍🙂


3. High-Level Architecture:

 
Frontend (Android App):
The Android app will handle all the user interactions such as login, calls, and notifications. It communicates directly with the backend via HTTP requests (using REST APIs provided by Spring Boot).
The frontend will interact with the backend by calling RESTful services for operations like authentication and sending/receiving calls.
Backend (Spring Boot Application):
The controller layer in Spring Boot handles HTTP requests from the Android app such as user logins, and call requests. 
The Service Layer contains the core business logic, such as handling user authentication, audio calls management, or file uploads. 
The Data Access Layer manages interactions with the database. This layer is responsible for persisting user data, messages, and files.
Database: The database will contain all user information, along with calls history.



4. Wireframing: 
https://www.figma.com/design/RRan7jt74IPU6Tv4aincH3/RideChat?node-id=0-1&t=Ob3pOCZKOE29bEcO-1
