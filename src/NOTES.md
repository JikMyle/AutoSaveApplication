# Development Notes

## 2026-01-09

### Progress
- Began recording developments note in file NOTES.md

#### BackupService
- **Description:** Dedicated service for handling file back up operations
- Created a function to check existing backup instances
- Created a function to delete old backup instances

### TODOS
- Possibly add filtering and name formats for backups
  - **REASON:** Backups are stored in a user-accessible directory;
  - User may attempt to create additional files/directories that may interfere with backup operations
- Learn and improve error handling in BackupService class
- Create file backup logic