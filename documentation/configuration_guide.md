# Configuration Guide IRPact
IRPact is designed as a framework for agent-based innovation diffusion models and is intended to cover a wide range of models.
It is based on flexible and modular design principles and identifies common mechanics and relations in models analysed for its design.

By this, a requirement for the software implementation of IRPact is a high degree of configurability. 
This has been achieved by devising a scheme for model configurations that is explained in the following.

Model configurations are stored in the ./target/configuration folder of the program, with a sub-folder containing the entire model configuration for each model.
Concrete models are invoked by providing the folder name of the configuration with the program call.
If, for example, you saved the configuration for your model in the *_fooConfiguration_* folder, you would invoke it by
```cmd
java -jar target/IRPactX.x.jar fooConfiguration
```

## Actor configuration
Actors are cognitive entities that can act proactively in the simulation, i.e. information agents.
Most actors are defined in individual files, describing each individual actor / agent group. 
As a singular actor, the policy agent is an exception to this.

Additionally, actor relations are configured in the *AgentConfiguration.json* file. 
This applies to the number of consumer agents in each consumer agent group, 
and the affinities between the consumer agents 
(i.e. how likely they are to send messages to agents of a certain group). 

The number of agents per group is parameterized through the *noAgentsPerGroup* key, 
with an object value that is a kv-pair of the name of the agent group and its (integer) value.

Key | Value
---|---
noAgentsPerGroup | Object
[*AgentGroupName*] | integer number  

The affinities of consumer agents are described with the *consumerAgentGroupAffinities* key,
which has an object value consisting of kv-pairs describing the respective group and its affinities.
The affinities of the group are themself an object consisting of the target agent group and a number describing the affinity.
Thus they are given through a hierarchical ordering of source-target group and its value.

Key | Value
---|---
consumerAgentGroupAffinities | [groupAffinity]Object
[*SourceAgentGroupName*] | [affinity]Object
[*TargetAgentGroupName*] | integer number

An exemplary configuration is given below: 

```json
{
	"noAgentsPerGroup" : {
		"Postmaterialists" : 2383,
		"SocialLeaders" : 2383,
		"Hedonists" : 2383,
		"Traditionalists" : 2383,
		"Mainstream" : 2383
	},
	"consumerAgentGroupAffinities" : {
	    "Postmaterialists" : {
	        "Postmaterialists" : 0.5,
	        "SocialLeaders" : 0.3,
	        "Hedonists" : 0.2,
	        "Traditionalists" : 0.0,
	        "Mainstream" : 0.0
	    },
	    "SocialLeaders" : {
	        "Postmaterialists" : 0.3,
	        "SocialLeaders" : 0.5,
	        "Hedonists" : 0.2,
	        "Traditionalists" : 0.0,
	        "Mainstream" : 0.0
	    },
	    "Hedonists" : {
	        "Postmaterialists" : 0.25,
	        "SocialLeaders" : 0.25,
	        "Hedonists" : 0.5,
	        "Traditionalists" : 0.0,
	        "Mainstream" : 0.0
	    },
	    "Traditionalists" : {
	        "Postmaterialists" : 0.1,
	        "SocialLeaders" : 0.5,
	        "Hedonists" : 0.0,
	        "Traditionalists" : 0.3,
	        "Mainstream" : 0.1
	    },
	    "Mainstream" : {
	        "Postmaterialists" : 0.1,
	        "SocialLeaders" : 0.5,
	        "Hedonists" : 0.0,
	        "Traditionalists" : 0.1,
	        "Mainstream" : 0.3
	    }
	}
}
```

### Consumer Agent Groups
Consumer agent groups are configured through individual files in the *consumerAgentGroup* folder. 
Each file represents a consumer agent group that must be matched in other configuration files,
with the name of the file being the name of the consumer group.

Each consumer agent group is configured through the following attributes:

Key | Value
---|---
consumerAgentGroupAttributes | Array of attributes
informationAuthority | number (float)
consumerAgentCommunicationScheme | [communicationScheme]Object
productGroupAwarenessDistribution | Object consisting of *productGroup*-*BooleanDistribution* pairs
fixedProductsAwarenessDistribution | Object consisting of *fixedProduct*-*BooleanDistribution* pairs
perceptionSchemeConfiguration | [perceptionScheme]Object
decisionProcessEmployed | *DecisionProcess*String
consumerAgentGroupValues | Object consisting of *Value*-*UnivariateDistribution pairs
spatialDistribution | Name of a *SpatialDistribution*
needDevelopmentScheme | Name of a *NeedDevelopmentScheme*
needIndicatorMap | Object of *Need*-number (float) pairs
initialProductConfiguration | Object of *fixedProduct*-*UnivariateDistribution* pairs

Of these, the productGroupAwarenessDistribution, the fixedProductsAwarenessDistribution, the spatialDistribution and the initialProductConfiguration have to be valid distributions, as parameterized in the respective *Distributions.json*  file.

#### Communication Scheme Configuration
A communication scheme is described through an object that contains the name of the communication scheme used
and the associated message scheme. 

Key | Value
---|---
communicationScheme | Identifier of a communication scheme
consumerAgentMessageScheme | [messageScheme]Object

The message scheme itself is configured through its identifier and the number of messages sent per time unit.

Key | Value
---|---
messageScheme | Identifier of a message scheme
numberMessagesSentPerTimeUnit | *UnivariateDistribution*

#### Perception Scheme Configuration
Perception schemes describe how product (group) attributes are perceived by the respective consumer group.
For each product group and their attributes, the perception scheme configuration parameterizes an individual perception scheme.
As these are rather heterogeneous, they are themselves described through an identifier and a set of parameters.
The perception scheme configuration is an object that contains as kv-pairs the identifier of the product group attribute,
and as a value an object of an attribute-object mapping, where the object itself looks according to the table?

Key | Value
---|---
perceptionScheme | Identifier of a perception scheme
parameters | Object of kv-pairs with perceptionscheme-specific entries


An exemplary consumer agent group configuration is shown below:
```json
{
	"consumerAgentGroupAttributes" : [
	],
	"informationAuthority" : 1.0,
	"consumerAgentCommunicationScheme" : {
		"communicationScheme" : "ImmediateCommunicationScheme",
		"consumerAgentMessageScheme" : {
			"messageScheme" : "DefaultConsumerAgentMessageScheme",
			"numberMessagesSentPerTimeUnit" : "noMessageDistribution"
		}
	},
	"productGroupAwarenessDistribution" : {
		"showerHeads" : "uniformlyTrue",
		"toiletFlushs" : "uniformlyTrue",
		"rainwaterUsageSystems" : "uniformlyTrue"
	},
	"fixedProductsAwarenessDistribution" : {
		"noRainwaterUsage": "uniformlyTrue",
		"rainwaterUsage" : "uniformlyTrue",
		"standardShowerHead" : "uniformlyTrue",
		"waterSaveShowerHead" : "uniformlyTrue",
		"standardFlush" : "uniformlyTrue",
		"stoppFlush" : "uniformlyTrue",
		"pressureFlush" : "uniformlyTrue",
		"twoCompartmentFlush" : "uniformlyTrue"
	},
	"perceptionSchemeConfiguration" :  {
		"showerHeads" : {
			"environmentalism" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"simplicity" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"compatibility" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"costEfficiency" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"investmentCost" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			}
		},
		"toiletFlushs" : {
			"environmentalism" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"simplicity" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"compatibility" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"costEfficiency" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"investmentCost" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			}
		},
		"rainwaterUsageSystems" : {
			"environmentalism" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"simplicity" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"compatibility" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"costEfficiency" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			},
			"investmentCost" : {
				"perceptionScheme" : "MemoryLessProductAttributePerceptionScheme",
				"parameters" : {					"initialPerception" : "diracDistribution1"				}
			}
		}
	},
	"decisionProcessEmployed" : "SchwarzTakeTheBest",
	"consumerAgentGroupValues" : {
			"environmentalism" : "diracDistribution0",
			"simplicity" : "diracDistribution0",
			"compatibility" : "diracDistribution035",
			"costEfficiency" : "diracDistribution01",
			"subjectiveNorm" : "diracDistribution0"
	},
	"spatialDistribution" : "dummySpatialDistribution",
	"needDevelopmentScheme" : "DefaultNeedDevelopmentScheme",
	"needIndicatorMap" : {
		"showering" : 0.004166666666666667,
		"rainwaterUsage" : 0.0013333333333333335,
		"toiletFlushing" : 0.0033333333333333335
	},
	"initialProductConfiguration" : {
		"standardShowerHead" : "diracDistribution1",
		"noRainwaterUsage" : "diracDistribution1",
		"rainwaterUsage" : "diracDistribution0" ,
		"standardFlush" : "diracDistribution05",
		"stoppFlush" : "diracDistribution01",
		"pressureFlush" : "diracDistribution04"
	}
} 
```
