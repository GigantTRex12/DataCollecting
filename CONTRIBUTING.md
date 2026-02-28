# Contributing

Anybody is free to contribute to this project in the form of ideas, bug reports or code. This serves as a short
guideline on how to contribute.

## Contributing Code

Contributions to code are always made via a pull request, either from a branch or fork. When contributing ensure that
all existing tests are still passing. You are free to use TDD, if you want.

### Small changes

For smaller changes it is fine to put multiple changes in the same pull request. Small changes are changes that are
fast and easy to fix and as fast to understand what was done. This includes but may not be limited to:

- Fixing typos
- Fixing code style
- Cleaning up code, for example removing unused import statements
- Fixing small bugs, which are fast to fix and where not very impactful

### New features

For new features please ensure that there is an open issue and that it is not labeled with "needs refinement". Feel
free to write an issue yourself, just be aware that working on issues that are not fully fleshed out may waste some
development time.  
When a new feature is finished, make sure to add tests to make sure they will still work after future changes. Perfect
Coverage isn't what's important, make sure the tests fill their purpose of ensuring functionality.

To keep this library clean some things are generally not considered a useful new feature/change. This doesn't mean
these things can never happen, but there would need to be a very good reason for these:

- Including a programming language other than Java
- Using some sort of big framework or extensive library; small libraries for e.g. mathematical functions are totally
fine, but any added library should fill a clear purpose and do so seamlessly
- Adding support for specific ways to store data; any way should be possible via an API but this is simply beyond the
scope of this library
- Adding translation to other natural languages

### Other

Other possible changes to code could for example include bigger bugfixes, enhancing performance or improving tests.
For changes like that which don't fall under small changes or new features consider if they make sense in the
context and simply add an issue (if it doesn't already exist) describing the change.  
Depending on the change you may want to extend existing tests (e.g. adding an edge case that was fixed).

### Pull Requests and commit messages

A pull request should contain all relevant changes that are being made, important here is what, not how. If it is
resolving an open issue make sure the branch/pull request is linked to the issue. In this case the issue may partly
serve as a description of what was changed. Double check that all requirements from the issue are met. Once a pull
request is approved, always squash&merge (if it hasn't already been done).  
If you want to create a Pull request on an unfinished state, to for example get feedback, you can do so, but please
mark it as WIP.


The style of commit messages is not very strict, as a reference see [here](https://chris.beams.io/git-commit). Key
points are:

- Include relevant changes
- Keep it short (max 50 characters)
- What, not how
- Capitalize subject line and don't end it with a period
- Use imperative mood (spoken or written as if giving a command or instruction)

Usually just the subject line should be enough here, but if you want to add more information you can add a body. Refer
to the above linked guide.

### Code Style

Generally standard conventions apply. This is just a list of the most relevant once or those that are not universally
agreed upon:

- KISS - Keep It Simple Stupid
- Less is more, if possible don't overcomplicate it
- Avoid workarounds, find the root of the issue
- Document *what* the code does with detailed Javadoc, this includes at least methods that can be seen and are meant
to be used when using this library. Use other comments sparsely
- Use camelCase/PascalCase over underscores
- Use linebreaks when chaining methods. Rule of thumb: no more than 1 method call with arguments per line
- Add linebreaks whenever a line gets too long
- Variable names should be descriptive, but prefer shorter names to full sentences
- Make sure to use encapsulation, at least non-static fields should be private/protected. Generally, visibility should
be limited to where it needs to be seen
- Keep everything (code, documentation...) in English

### Tests

For writing new tests try to follow the Schema of existing ones. Generally Integration Tests are preferred, but for
small, often used functionality like Utilities Unit Tests are fine.

## Writing Issues

There is no strict Format for writing issues, just a few things that are, depending on the case, often good to have.  
Generally also ensure that this issue does not exist, rather reopen an old issue with a comment than writing a new one.
Reference to other issues where relevant and keep everything in English.

### New features

Issues for new features should be as descriptive as possible so that there aren't any misunderstandings. This is not
always easy, so just be open for questions. If you are aware that information is missing for any reason, make sure to
add the "needs refinement" label. For new features generally also add the "enhancement" label. An issue may also
include:

- A checklist of what new things should be done
- Implementation details, if known, that may be relevant (e.g. names of classes that need to be changed or order of
operations)
- Limitations, what the new feature should NOT do

### Bug Fixes

Before reporting a bug as an issue, ensure that it is actually unintended behaviour. If it is not clear, if it's
actually a bug then add the "unclear behaviour" label or the "documentation" label if it's likely just lack of
documentation, otherwise add the "bug" label. Make sure to include the following things:

- Steps to reproduce the issue
- A minimal case (code snippet) where the bug happens
- Expected behaviour
- Actual behaviour

### Other

Generally ensure that the description is clear and to add the relevant tag, here likely "enhancement", "documentation"
or "other".