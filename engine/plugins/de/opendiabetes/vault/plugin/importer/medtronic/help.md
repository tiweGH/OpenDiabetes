# Sample Plugin
ver 1.0.1
Classification: Importer

Overview
-----
Sample plugin is an importer plugin which imports data in the sample.smpl format.

Data example
-----
The sample.smpl format ist a format used by the Sample Corp. and example can be found here: http://example.com/sample.smpl

Configuration
-----
The Sample plugin offers the following configuration options:

| key  | value | description | required |
| ------------- | ------------- |  ------------- | ------------- |
| fromDate | DD.MM.YYYY | The date from when the data in the provided smple file should be imported | x
| toDate | DD.MM.YYYY | The date until when the data in the provided smpl file should be imported |

Required Plugins
-----
 - CSVImporterPlugin (http://example.com/CSVImporter)
