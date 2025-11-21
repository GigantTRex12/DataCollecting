# Data Collection & Analyzation

This library provides 2 functionalities:  

- Creating and storing Data from user inputs.
- Analyzing the saved Data by giving human-readable outputs representing certain aspects about the data.

This library makes no assumptions about how the data is stored, so it doesn't contain logic for saving and reading the
data itself, only processing it.  
Currently only inputs and outputs via command line are supported.

## Core Classes:

These Classes are the core of this library, and they're meant to be extended for usage and customization.

- [BaseDataSet](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/dataset/BaseDataSet.java): Base
Class for the structure of the collected Data.
- [BaseDataCollector](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/collector/BaseDataCollector.java):
Base Class for collecting Data.
- [BaseDataAnalyzer](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/analyzer/BaseDataAnalyzer.java):
Base Class for analyzing Data.

## Usage

For a simplified example usage see [Tests](https://github.com/GigantTRex12/DataCollecting/tree/master/src/test/java/example).

### Create DataStructure

Extend the [BaseDataSet](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/dataset/BaseDataSet.java)
Class to create the structure of the Data. Add any relevant constructors/getters.  
If the Data may simply be stored in a key-value map usages of the [MapDataSet](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/dataset/MapDataSet.java)
Class may simplify things.  

#### -MetaData-

For any Fields that might be shared among multiple different types of DataSets the [MetaData](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/dataset/Metadata.java)
Class may _optionally_ be extended. MetaData may hold any shared Data like for example timestamps, versions etc.

### Create DataCollector

Extend the [BaseDataCollector](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/collector/BaseDataCollector.java)
Class (or [MapDataCollector](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/collector/MapDataCollector.java)
if using [MapDataSet](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/dataset/MapDataSet.java))
and implement the mapToDataset() (not needed for [MapDataCollector](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/collector/MapDataCollector.java))
and saveData() methods. If using [MetaData](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/dataset/Metadata.java)
setMetadata() should also be overridden.  
If using this for multiple different DataSets the created implementation of [BaseDataCollector](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/collector/BaseDataCollector.java)
may be further extended once for each DataSet. Then implement the getQuestions() method for each DataSet. The Questions
can easily be created with a Builder, see [Question](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/collector/Question.java)
for details.  
Optionally validateDataSet() may be overridden as needed. To add or modify actions the [ActionMap](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/Utils/ActionMap.java)
actions can be modified. Note that any methods in the DataCollector are allowed to be overridden, however
the use-cases of doing so might be limited.

### Create Analyzer

Extend the [BaseDataAnalyzer](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/analyzer/BaseDataAnalyzer.java)
Class and implement a constructor which reads the Data (or alternatively just pass the Data to super() if reading the
Data happens outside the Analyzer). Then implement the getQuestions() method for each DataSet. The Questions
can easily be created with a Builder, see [Question](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/analyzer/Question.java)
for details.  
To add or modify actions the [ActionMap](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/Utils/ActionMap.java)
actions can be modified. Note that any methods in the DataCollector are allowed to be overridden, however the
use-cases of doing so might be limited.

#### -Evaluators-

Some static evaluators are provided by the [BaseDataAnalyzer](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/analyzer/BaseDataAnalyzer.java)
but any other needed evaluators may be implemented. Currently available evaluators are:

- SIMPLE_PERCENTAGES: Simply counts how many times certain Objects appear in a List and calculates their percentages.
- WILSON_CONFIDENCE: Calculates the percentages like SIMPLE_PERCENTAGES but also calculates a [Wilson Score Interval](https://en.wikipedia.org/wiki/Binomial_proportion_confidence_interval">https://en.wikipedia.org/wiki/Binomial_proportion_confidence_interval)
with a confidence of 95%.
- WILSON_CONFIDENCE_COUNTER: Like WILSON_CONFIDENCE but takes an already filled [Counter](https://github.com/GigantTRex12/DataCollecting/blob/master/src/main/java/Utils/Counter.java)
instead.
