# The Challenge
At Twitter, authors of source code changes must get approval from the engineers responsible for the files
affected by the change. There are systems in place to ensure that changes have the required approvals.


For this challenge, we're going to ask you to implement a simplified version of our approval system. This version
of the approval system uses two types of files. Each directory in the repository may contain one or both of them.
They contain the information used to identify who must approve a change. They are DEPENDENCIES and
OWNERS.


## **DEPENDENCIES files**
These files contain a line delimited list of paths. These paths are the directories containing sources that the
current directory's sources depend on. Paths must be relative to the root directory of the source code repository.


## **OWNERS files**
These files contain a line delimited list of usernames of engineers. The usernames refer to the engineers who
can approve changes affecting the containing directory and its subdirectories. If there is no OWNERS file or it is
empty, then the parent directory's OWNERS file should be used.


### **Approval rules **
* A change is approved if all of the affected directories are approved.
* A directory is considered to be affected by a change if either a file in that directory was changed, or a file in
one of its dependencies was changed. The second case also includes transitive changes, so if a
dependency of a dependency changes, the current directory is affected.
* A directory has approval if at least one engineer listed in an OWNERS file in it or any of its parent
directories has approved it.


For example, consider the following directory tree:

```
 x/
    DEPENDENCIES = "y\n"
    OWNERS = "A\nB\n"
  y/
    OWNERS = "B\nC\n"
    file
``` 

If a change modifies y/file, it affects both directories y (contains file) and x (depends on y). This change must at a
minimum be approved by either B alone (owner of x and y) or both A (owner of x) and C (owner of y).


### **Program Requirements**
Build a command line utility called validate_approvals that validates that the correct people have approved
changes to a set of files.

It will take arguments via two flags, --approvers and --changed-files. Both flags arguments are comma separated.

As an example, the following is expected to work on the example directory structure we have provided to you.

```
$ validate_approvals --approvers alovelace,ghopper --changed-files
src/com/twitter/follow/Follow.java,src/com/twitter/user/User.java
Approved
$ validate_approvals --approvers alovelace --changed-files
src/com/twitter/user/User.java
Insufficient approvals
```


## Expectations
* The challenge should take about three hours to complete.
* Language
  * If possible use one of Scala, Java, Python, Javascript, C, or C++.
  * Let us know if you'd like to use another language, so we can figure out whether we have the
    expertise to grade it fairly.
* Documentation
  * README with instructions for building the program, running the program, and running its tests.
  * Appropriate comments for the problem in the implementation.
* Testing
  * A suite of automated tests, that exercises the implementation.

