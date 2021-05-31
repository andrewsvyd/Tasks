## Project structure

 - **data**

	A layer where all of the data logic belongs. Basically, here you can find repositories, which 		are capable of providing the data. There are all of the data base classes as well

	 - **networking**
	Base classes for network, i.e. Retrofit, OkHttp, etc.
	 - **persistence**
	 Interface for DAO, and it's implementation with underlying DB

	 - **repository**
		 Each sub package represents repository interfaces and implementations for specific modules

 - **presentation**
	 A layer which represents UI dependencies of project. Implemented as an MVP


	 - **base**
		 Base classes for MVP and Android components


	 - **exception**
		 Error handling, implemented as a delegation model

	 - **features**
		 Represents each screen of application as sub packages

## Dependency Injection
There is no DI framework integrated in project, such as Dagger 2. Instead, there is an alternative implementation of DI used. PresenterProvider class takes care of the creation of all needed dependencies. This is the only place where you create concrete implementations of dependencies (just like Module from Dagger 2).
