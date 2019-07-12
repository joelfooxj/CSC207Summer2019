Additional constraints:
- Applicant cannot reapply to the same job after withdrawing their application.
- An application is set to closed if an applicant withdraws, or if they're hired/rejected.
-

Notes on using the setup files:

Database files are saved into .bin in the src folder.
TestApplication.bin, TestFirms.bin, TestJobs.bin, and TestUsers.bin files are pre-populated database files.
At the prompt "Do you want to import the default test setup? (y/n):", selecting 'y' will import pre-created User and
Firm objects into the program. Selecting 'n' will create new .bin files for this session.

At the prompt "Do you want to overwrite the default test setup? (y/n): ", selecting 'y' will overwrite
the TestObject.bin files with the current state of the databases at the conclusion of every session,
while selecting 'n' will overwrite the newly created (or pre-existing) Object.bin files.





