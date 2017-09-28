Validation Approvals
====
Checks if a changed file can be approved from a provided list of approvers.
Directories are owned by users (defined in a `USERS` file) and directories have package/directory
dependencies (defined in the `DEPENDENCIES`) 

The rules of approvals are as follows:
* A change is approved if all of the affected directories are approved.
* A directory is considered to be affected by a change if either a file in that directory was changed, or a file in
one of its dependencies was changed. The second case also includes transitive changes, so if a
dependency of a dependency changes, the current directory is affected.
* A directory has approval if at least one engineer listed in an OWNERS file in it or any of its parent
directories has approved it.


# Usage
```
--validate_approvers --approvals <comma-delimited-approvers> --changed-files <comma-delimited-changed-files>
```

```
# Examples

$ validate_approvals --approvers alovelace,ghopper --changed-files
src/com/twitter/follow/Follow.java,src/com/twitter/user/User.java
Approved

$ validate_approvals --approvers alovelace --changed-files
src/com/twitter/user/User.java
Insufficient approvals
```

# Installation
Requires SBT to build.
Main application code is at: `ValidateApprovals.scala`


# Main modules
1. CLI parsing: `ValidatorCLI.scala`
2. Main application: `ValidateApprovals.scala`
3. Directory dependency model: `Digraph.scala`


Directory trait
* may contain a `DEPENDENCIES` file
* may contain a `USERS` file
* may have a dependent directory defined in DEPENDENCIES file
  * approval cannot occur unless we check it's ownership
    * ownership can occur in two cases:
       1. root owner listed in OWNERS file
       2. subdirectory owners listed
* may contain a USERS file in parent directory
  * The assumption is that our owner directory above the listed path
    is not a parent 
  * The assumption is that our owner directory is the dependent
    file root

# Test suite
`DependencyDigraphTest.scala`