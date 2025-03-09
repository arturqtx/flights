1. The task doesn't specify what exactly "arrival date" is. In my implementation, I assumed that it is a departure time of the inbound (return) leg.

2. The CrazyAir API uses local timestamps. I assumed these timestamps correspond to the local timezones of the airports.
   In my implementation, I adjusted the timestamps to GMT (UTC) using the respective airport timezones.
   In contrast, other APIs return timestamps in GMT (UTC).

3. The task recommends using Spring Data; however, I did not find an immediate use for it.
   Adding persistence now would add unnecessary complexity, over-engineering it.

4. The current implementation lacks fault tolerance for API failures. In case of supplier's outage, the entire Deblock APIs fails.
   A potential improvement could involve eliminating the single point of failure, ensuring that the system can still return results if at least one API is functional.

   In the long term, I would consider fully separating external API calls from the user-facing path. The user would submit a request, which would be persisted in the database.
   The system would then emit an event (to Kafka), and the user would receive a request ID to track the progress, either through polling or websockets.

   Backend workers would receive the request event and begin fetching flight data from external APIs. As the data is fetched, it would be fed into the user's request.

   If an external API call fails, the user would not be impacted. The worker can retry the request, ensuring the system remains resilient. This approach would work
   even better if we maintained a cache / offline flights data (perhaps outdated) that would then be re-freshen by the workers.
