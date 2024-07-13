This was a Google coding challenge problem of a banksystem involving customers, a customer queue and tellers.
It was a basic producer/consumer problem.
Other than the object and variable names, it was implemented in a fairly generic and flexible way.
There are 2 high level subsystems to the application:
1. producer/(pool of) consumers
2. eventbus - based upon the observer pattern with a threadpool for dispatching to the registered event handlers asyncronously.

The design allows for configuration of different types of customer producers in order to mimic different bank customer traffic patterns.
There is only 1 type of customer (or only 1 type of service needed), so all customers currently need the same processing time.
However, the design considers that different customer types may be added.

There is a BranchOfficer of the Bank which manages the size of the pool of consumers (ie the tellers) and makes decisions on the pool size
based upon one of any number of strategies. There is only one teller type at this point. There are currently no specialty tellers to handle
different types of customer services or have different levels of seniority/efficiency.

The crux of the initial problem is that:
1. the customer queue is of some fixed size.
2. There is a cost of losing a customer if they can't get on the queue.
3. There is a cost for having tellers who were already scheduled to work
4. There is an additional cost if tellers need to be called in in order to minimize the costs of losing customers

