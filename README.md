# DIRAP: Data-Ingestion-and-Runtime-Analysis-Pipeline

DIRAP is an end-to-end data ingestion and runtime processing pipeline that forms a core part of a technical solution aimed 
at monitoring frailty indices of patients during and after treatment to improve their quality of life. It uses open source 
systems designed specifically for big data applications, making it scalable and fault-tolerant by design.


## Architecture
A general overview of the whole architecture is presented below:

<div style="text-align:center">
<img src="https://github.com/LifeChamps/AUTH_CSD_DIP/SystemOverview.png" width="300" height="600" alt="">
</div>


The pipeline ingests data not only from multiple sensor types but also from electronic health records (EHR). 
Data from multiple sensors, including data from FitBit and location sensors, is collected in the Edge Component and 
securely transferred to MQTT. From there, data is preprocessed in real-time by the Streaming Component (blue), 
which is implemented in Apache Flink, and finally ingested into the Message Bus responsible for communication between 
the various components, implemented with Apache Kafka.
Streaming Component is located in the **cloud** project.

Additionally, to facilitate the ingestion of EHR data from medical partners, the Batch Component was implemented (red). 
This component encapsulates several APIs that accept EHR records in JSON format, as well as other forms of batch data, 
and ingests them into the appropriate topics of the Message Bus. Batch Component is located in the **lc_api** folder.

## Deployment
All the implemented components and the used infrastructure is containerized to make the deployment easier.

The infrastructure contains an instance of MQTT, which runs over SSL protocol, an Apache Kafka, along
with the required zookeeper and a database
for permanent storage (in this implementation we choose InfluxDB, which is suited for time series).
To deploy the whole infrastructure run
```docker-compose -f dockerbase/docker-compose-inf.yml up```

In order to run the aforementioned components execute the command ```docker-compose -f dockerbase/docker-compose-code.yml up```. 
This will build and run the Batch Component and also deploy a Flink jobmanager and a Flink taskmanager that are required
for the Streaming Component.

Finally, the whole premise of DIRAP is to be extensible, in order to match new needs, like integrating additional sensors.
The file located in **cloud/src/main/resources/pipelines.json** describe all the different pipelines that will be created. 
This effectively minimizes the interaction of the users with the actual code and make DIRAP applicable in a wider range of 
applications.

Our complete work can be found [here](https://link.springer.com/article/10.1007/s13278-022-00891-y).

