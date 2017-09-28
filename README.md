Validation Approvals
====
Approval and dependency management for checking approvers within

# Usage
```
--validate_approvers
```

# Installation
Run the compiler script
```

```

User Entities
* Users (User model)
* Acceptors (Acceptor Model)

Project directories
* may contain a DEPENDENCIES file
* may contain a USERS file

Directory trait
* may contain a `DEPENDENCIES` file
* may contain a `USERS` file
* may have a dependent directory in DEPENDENCIES file
  * approval cannot occur unless we check it's ownership
    * ownership can occur in two cases:
       1. root owner listed in OWNERS file
       2. subdirectory owners listed //TODO: verify
* may contain a USERS file in parent directory
  * The assumption is that our owner directory above the listed path
    is not a parent (?) //TODO
  * The assumption is that our owner directory is the dependent
    file root

# Test suite
