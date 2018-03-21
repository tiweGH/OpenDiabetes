# Copyright (C) 2017 Jens Heuschkel, Philipp Thomasberger
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
from numpy.distutils.exec_command import temp_file_name

try:
    import csv
    import datetime
    import json
    import matplotlib.pyplot as plt
    import matplotlib.dates as dates
    import matplotlib.lines as lines
    import matplotlib.patches as patches
    import matplotlib.transforms as transforms
    from matplotlib.ticker import MultipleLocator
    import re
    import os.path
    import sys
    import shutil
    import pylab
    import numpy as np
    from optparse import OptionParser
except ImportError as err:
    print "[ModuleError]", err, "(needed libraries: matplotlib, numpy)"
    exit(0)
# const
# TODO check required fields only

csvDataFormat = ['date','time','bgValue','cgmValue','cgmRawValue','cgmAlertValue','glucoseAnnotation','basalValue','basalAnnotation','bolusValue','bolusAnnotation','bolusCalculationValue','mealValue','pumpAnnotation','exerciseTimeValue','exerciseAnnotation','heartRateValue','heartRateVariabilityValue','stressBalanceValue','stressValue','sleepValue','sleepAnnotation','locationAnnotation', 'mlCgmValue', 'pumpCgmPredictionValue', 'otherAnnotation']
slicesDataFormat = ['date','time','duration','type','filter']

## helper functions
def dateParser(date, time):
    return datetime.datetime.strptime(date + time, '%d.%m.%y%H:%M')

def absPathOnly():
    return os.path.dirname(os.path.realpath(sys.argv[0]))
def absPath(filename):
    return os.path.join(absPathOnly(), filename)

def parseDataset(csvFileName):
    with open(csvFileName, 'rU') as csvfile:
        reader = csv.reader(csvfile, delimiter=',', dialect=csv.excel_tab)
        try:
            header = reader.next()
        except StopIteration:
            print "[Error] Given dataset is empty"
            exit(0)

        for headerField in csvDataFormat:
            if headerField not in header:
                print '[FormatError] File \'' + csvFileName + '\' has a wrong header'
                exit(0)

        dataset = []

        for row in reader:
            dataset.append(dict(zip(header, row)))

    if not dataset:
        print "[Error] Given dataset is empty"
        exit(0)

    return dataset

def dataSubset(dataset, beginDate, duration):
    endDate = beginDate + datetime.timedelta(minutes=duration)

    locationCache = ""
    stressCache = ""
    sleepCache = ""
    basalCache = ""
    exerciseCache = ""

    datasubset = []

    cacheValuesExist = False

    for d in dataset:
        tempDate = dateParser(d['date'], d['time'])
        if tempDate < beginDate:
            if d['locationAnnotation']:
                locationCache = {'pumpAnnotation': '', 'cgmAlertValue': '', 'mealValue': '', 'basalValue': '',
                               'bolusValue': '', 'sleepValue': '', 'exerciseAnnotation': '', 'heartRateValue': '',
                               'cgmRawValue': '', 'bolusAnnotation': '', 'cgmValue': '', 'bolusCalculationValue': '',
                               'basalAnnotation': '', 'locationAnnotation': '', 'stressValue': '',
                               'heartRateVariabilityValue': '', 'date': beginDate.strftime("%d.%m.%y"),
                               'stressBalanceValue': '', 'glucoseAnnotation': '', 'bgValue': '',
                               'exerciseTimeValue': '', 'time': '00:00', 'sleepAnnotation': d['locationAnnotation'], 'mlCgmValue': '', 'pumpCgmPredictionValue': '',
                               'otherAnnotation': ''}
                cacheValuesExist = True
            if d['stressValue']:
                stressCache = {'pumpAnnotation': '', 'cgmAlertValue': '', 'mealValue': '', 'basalValue': '',
                               'bolusValue': '', 'sleepValue': '', 'exerciseAnnotation': '', 'heartRateValue': '',
                               'cgmRawValue': '', 'bolusAnnotation': '', 'cgmValue': '', 'bolusCalculationValue': '',
                               'basalAnnotation': '', 'locationAnnotation': '', 'stressValue': d['stressValue'],
                               'heartRateVariabilityValue': '', 'date': beginDate.strftime("%d.%m.%y"),
                               'stressBalanceValue': '', 'glucoseAnnotation': '', 'bgValue': '',
                               'exerciseTimeValue': '', 'time': '00:00', 'sleepAnnotation': '', 'mlCgmValue': '', 'pumpCgmPredictionValue': '',
                               'otherAnnotation': ''}
                cacheValuesExist = True
            if d['sleepAnnotation']:
                if dateParser(d['date'], d['time']) + datetime.timedelta(minutes=float(d['sleepValue'])) > beginDate:
                    sleepCache = {'pumpAnnotation': '', 'cgmAlertValue': '', 'mealValue': '', 'basalValue': '',
                                   'bolusValue': '', 'sleepValue': (float(d['sleepValue']) - ((beginDate - dateParser(d['date'], d['time'])).total_seconds() / 60)), 'exerciseAnnotation': '', 'heartRateValue': '',
                                   'cgmRawValue': '', 'bolusAnnotation': '', 'cgmValue': '', 'bolusCalculationValue': '',
                                   'basalAnnotation': '', 'locationAnnotation': '', 'stressValue': '',
                                   'heartRateVariabilityValue': '', 'date': beginDate.strftime("%d.%m.%y"),
                                   'stressBalanceValue': '', 'glucoseAnnotation': '', 'bgValue': '',
                                   'exerciseTimeValue': '', 'time': '00:00', 'sleepAnnotation': d['sleepAnnotation'], 'mlCgmValue': '', 'pumpCgmPredictionValue': '',
                                   'otherAnnotation': ''}
                cacheValuesExist = True
            if d['basalValue']:
                basalCache = {'pumpAnnotation': '', 'cgmAlertValue': '', 'mealValue': '', 'basalValue': d['basalValue'],
                               'bolusValue': '', 'sleepValue': '', 'exerciseAnnotation': '', 'heartRateValue': '',
                               'cgmRawValue': '', 'bolusAnnotation': '', 'cgmValue': '', 'bolusCalculationValue': '',
                               'basalAnnotation': d['basalAnnotation'], 'locationAnnotation': '', 'stressValue': '',
                               'heartRateVariabilityValue': '', 'date': beginDate.strftime("%d.%m.%y"),
                               'stressBalanceValue': '', 'glucoseAnnotation': '', 'bgValue': '',
                               'exerciseTimeValue': '', 'time': '00:00', 'sleepAnnotation': '', 'mlCgmValue': '', 'pumpCgmPredictionValue': '',
                               'otherAnnotation': ''}
                cacheValuesExist = True
            if d['exerciseAnnotation']:
                if d['exerciseTimeValue'] and dateParser(d['date'], d['time']) + datetime.timedelta(minutes=float(d['exerciseTimeValue'])) > beginDate:
                    exerciseCache = {'pumpAnnotation': '', 'cgmAlertValue': '', 'mealValue': '', 'basalValue': '',
                                   'bolusValue': '', 'sleepValue': '', 'exerciseAnnotation': d['exerciseAnnotation'], 'heartRateValue': '',
                                   'cgmRawValue': '', 'bolusAnnotation': '', 'cgmValue': '',
                                   'bolusCalculationValue': '', 'basalAnnotation': '', 'locationAnnotation': '',
                                   'stressValue': '', 'heartRateVariabilityValue': '',
                                   'date': beginDate.strftime("%d.%m.%y"), 'stressBalanceValue': '',
                                   'glucoseAnnotation': '', 'bgValue': '', 'exerciseTimeValue': (float(d['exerciseTimeValue']) - ((beginDate - dateParser(d['date'], d['time'])).total_seconds() / 60)), 'time': '00:00',
                                   'sleepAnnotation': '', 'mlCgmValue': '', 'pumpCgmPredictionValue': '',
                                   'otherAnnotation': ''}
                cacheValuesExist = True
        elif tempDate >= beginDate and tempDate < endDate:
            if cacheValuesExist:
                cacheValuesExist = False
                if locationCache:
                    datasubset.append(locationCache)
                if stressCache:
                    datasubset.append(stressCache)
                if sleepCache:
                    datasubset.append(sleepCache)
                if basalCache:
                    datasubset.append(basalCache)
                if exerciseCache:
                    datasubset.append(exerciseCache)
            datasubset.append(d)

    return datasubset

def findLimits(dataset, config):
    limits = {}
    limits['maxBarValue'] = config['maxBarValue']
    limits['minCgmBgValue'] = config['minCgmBgValue']
    limits['maxBasalValue'] = config['maxBasalValue']
    limits['maxBasalBelowLegendValue'] = config['maxBasalBelowLegendValue']
    limits['minHrValue'] = config['minHrValue']
    limits['maxHrValue'] = config['maxHrValue']

    if not config['limitsManual']:
        for d in dataset:
            if d['bgValue']:
                if float(d['bgValue']) < limits['minCgmBgValue']:
                    limits['minCgmBgValue'] = float(d['bgValue'])
            if d['cgmValue']:
                if float(d['cgmValue']) < limits['minCgmBgValue']:
                    limits['minCgmBgValue'] = float(d['cgmValue'])
            if d['basalValue']:
                if float(d['basalValue']) > limits['maxBasalValue']:
                    if dateParser(d['date'], d['time']) < dateParser(d['date'], d['time']).replace(hour=04, minute=00):
                        limits['maxBasalBelowLegend'] = float(d['basalValue'])
                    limits['maxBasalValue'] = float(d['basalValue'])
            if d['bolusValue']:
                if float(d['bolusValue']) > limits['maxBarValue']:
                    limits['maxBarValue'] = float(d['bolusValue'])
            if d['mealValue']:
                if float(d['mealValue']) > limits['maxBarValue']:
                    limits['maxBarValue'] = float(d['mealValue'])
            if d['heartRateValue']:
                if float(d['heartRateValue']) < limits['minHrValue']:
                    limits['minHrValue'] = float(d['heartRateValue'])
                if float(d['heartRateValue']) > limits['maxHrValue']:
                    limits['maxHrValue'] = float(d['heartRateValue'])

    return limits

def linregressScipy(x, y):
    x = np.asarray(x)
    y = np.asarray(y)

    if x.size == 0 or y.size == 0:
        raise ValueError("[error] Inputs must not be empty.")

    xmean = np.mean(x, None)
    ymean = np.mean(y, None)

    # average sum of squares:
    ssxm, ssxym, ssyxm, ssym = np.cov(x, y, bias=1).flat
    r_num = ssxym

    slope = r_num / ssxm
    intercept = ymean - slope*xmean
    return (slope, intercept)

def parseConfig(configFileName):
    with open(configFileName) as config_file:
        jsonconfig = json.load(config_file)
    return jsonconfig

def parseSlices(csvFileName):
    with open(csvFileName, 'rU') as csvfile:
        reader = csv.reader(csvfile, delimiter=',', dialect=csv.excel_tab)
        header = reader.next()

        if slicesDataFormat != header:
            print '[FormatError] File \'' + csvFileName + '\' has a wrong header'
            exit(0)

        slices = []

        for row in reader:
            slices.append(dict(zip(header, row)))

    return slices

rewindAvailable = False
katErrAvailable = False
exerciseAvailable = False
cgmCalibrationErrorAvailable = False
cgmConnectionErrorAvailable = False
cgmSensorFinishedAvailable = False
cgmSensorStartAvailable = False
cgmTimeSyncAvailable = False
pumpReservoirEmptyAvailable = False


def prepareDataset(dataset, config, beginDate, endDate):
    global rewindAvailable
    global katErrAvailable
    global exerciseAvailable
    global cgmCalibrationErrorAvailable
    global cgmConnectionErrorAvailable
    global cgmSensorFinishedAvailable
    global cgmSensorStartAvailable
    global cgmTimeSyncAvailable
    global pumpReservoirEmptyAvailable

    plottingData = {'basalValuesX':[], 'basalValuesY':[],
                    'mergedBolusX':[], 'mergedBolusY':[],
                    'mergedCarbX':[], 'mergedCarbY':[],
                    'bolusSquareValuesX':[], 'bolusSquareValuesY':[],
                    'sleepTouples':[],
                    'cgmValuesX':[[]], 'cgmValuesY':[[]],
                    'cgmValuesRedX': [], 'cgmValuesRedY': [],
                    'cgmAlertValuesX':[], 'cgmAlertValuesY':[],
                    'cgmAlertValuesRedX':[], 'cgmAlertValuesRedY':[],
                    'cgmRawValuesX':[[]], 'cgmRawValuesY':[[]],
                    'bgValuesX':[], 'bgValuesY':[],
                    'bolusCalculationValuesX':[], 'bolusCalculationValuesY':[],
                    'heartRateValuesX':[[]], 'heartRateValuesY':[[]],
                    'pumpRewindX':[], 'pumpRewindY':[],
                    'pumpKatErrX':[], 'pumpKatErrY':[],
                    'MarkerExerciseX':[], 'MarkerExerciseY':[],
                    'exerciseX':[], 'exerciseY':[], 'exerciseColor':[],
                    'stressX':[], 'stressY':[],
                    'locationX':[], 'locationY':[],
                    'mlCgmValuesX':[[]], 'mlCgmValuesY':[[]],
                    'pumpCgmPredictionValuesX': [[]], 'pumpCgmPredictionValuesY': [[]],
                    'cgmCalibrationValuesX':[], 'cgmCalibrationValuesY':[],
                    'MarkerCgmCalibrationErrorX': [], 'MarkerCgmCalibrationErrorY': [],
                    'MarkerCgmConnectionErrorX': [], 'MarkerCgmConnectionErrorY': [],
                    'MarkerCgmSensorFinishedX': [], 'MarkerCgmSensorFinishedY': [],
                    'MarkerCgmSensorStartX': [], 'MarkerCgmSensorStartY': [],
                    'MarkerCgmTimeSyncX': [], 'MarkerCgmTimeSyncY': [],
                    'MarkerPumpReservoirEmptyX': [], 'MarkerPumpReservoirEmptyY': [],
                    'suspendTouples':[],
                    'glucoseElevationValuesX':[], 'glucoseElevationValuesY':[], 'glucoseElevationValuesCGM':[],
                    'cgmValuesTotal':0, 'cgmValuesBelow':0, 'cgmValuesAbove':0, 'basalTotal':0, 'bolusTotal':0,
                    'carbTotal':0, 'autonomousSuspensionTotal':datetime.timedelta(minutes=0), 'noteAnnotation':"",
                    'cgmValuesInRange':0}

    # Temp
    bolusValuesX = []
    bolusValuesY = []
    carbValuesX = []
    carbValuesY = []
    suspendTemp = []
    cgmIndex = 0
    cgmRawIndex = 0
    cgmMlIndex = 0
    pumpCgmPredictionIndex = 0
    hrIndex = 0
    prevCgmDate = ""
    prevCgmRawDate = ""
    prevCgmMlDate = ""
    prevHrDate = ""

    prevBasalValue = ""
    prevBasalDate = ""

    for d in dataset:
        tempDate = dateParser(d['date'], d['time'])
        if d['basalValue']:
            if prevBasalValue and prevBasalDate:
                plottingData['basalTotal'] += (((tempDate - prevBasalDate).total_seconds() / 3600) * float(prevBasalValue))
            prevBasalValue = d['basalValue']
            prevBasalDate = tempDate
            plottingData['basalValuesX'].append(tempDate)
            plottingData['basalValuesY'].append(d['basalValue'])
        if d['bolusValue']:
            plottingData['bolusTotal'] += float(d['bolusValue'])
            if "BOLUS_SQARE" not in d['bolusAnnotation']:
                bolusValuesX.append(tempDate)
                bolusValuesY.append(d['bolusValue'])
            else:
                for a in re.split(config['delimiter'], d['bolusAnnotation']):
                    if "BOLUS_SQARE" in a:
                        plottingData['bolusSquareValuesX'].append(tempDate)
                        plottingData['bolusSquareValuesY'].append((d['bolusValue'], re.split("=", a)[1]))
        if d['mealValue']:
            carbValuesX.append(tempDate)
            carbValuesY.append(d['mealValue'])
            plottingData['carbTotal'] += float(d['mealValue'])
        if d['sleepAnnotation']:
            if d['sleepAnnotation'] == "SLEEP_DEEP":
                plottingData['sleepTouples'].append((tempDate, tempDate + datetime.timedelta(minutes=float(d['sleepValue'])), config['deepSleepColor']))
            if d['sleepAnnotation'] == "SLEEP_LIGHT":
                plottingData['sleepTouples'].append((tempDate, tempDate + datetime.timedelta(minutes=float(d['sleepValue'])), config['lightSleepColor']))
        if d['cgmValue']:
            if prevCgmDate and ((tempDate - prevCgmDate) > datetime.timedelta(minutes=config['interruptLinePlotMinutes'])):
                cgmIndex += 1
                plottingData['cgmValuesX'].append([])
                plottingData['cgmValuesY'].append([])
            prevCgmDate = tempDate
            plottingData['cgmValuesX'][cgmIndex].append(tempDate)
            if float(d['cgmValue']) > config['bgCgmMaxValue']:
                plottingData['cgmValuesY'][cgmIndex].append(config['cgmBgHighLimit'] - 1)
                plottingData['cgmValuesRedX'].append(tempDate)
                plottingData['cgmValuesRedY'].append(config['cgmBgHighLimit'] - 1)
            else:
                plottingData['cgmValuesY'][cgmIndex].append(d['cgmValue'])
        if d['cgmAlertValue']:
            plottingData['cgmAlertValuesX'].append(tempDate)
            if float(d['cgmAlertValue']) > config['bgCgmMaxValue']:
                plottingData['cgmAlertValuesY'].append(config['cgmBgHighLimit'] - 1)
                plottingData['cgmAlertValuesRedX'].append(tempDate)
                plottingData['cgmAlertValuesRedY'].append(config['cgmBgHighLimit'] - 1)
            else:
                plottingData['cgmAlertValuesY'].append(d['cgmAlertValue'])
        if d['cgmRawValue']:
            if prevCgmRawDate and ((tempDate - prevCgmRawDate) > datetime.timedelta(minutes=config['interruptLinePlotMinutes'])):
                cgmRawIndex += 1
                plottingData['cgmRawValuesX'].append([])
                plottingData['cgmRawValuesY'].append([])
            prevCgmRawDate = tempDate
            plottingData['cgmRawValuesX'][cgmRawIndex].append(tempDate)
            plottingData['cgmRawValuesY'][cgmRawIndex].append(d['cgmRawValue'])
        if d['bgValue']:
            if not(d['glucoseAnnotation'] == "GLUCOSE_BG_MANUAL"):
                plottingData['bgValuesX'].append(tempDate)
                if float(d['bgValue']) > config['bgCgmMaxValue']:
                    plottingData['bgValuesY'].append(config['cgmBgHighLimit'] - 1)
                else:
                    plottingData['bgValuesY'].append(d['bgValue'])
        if d['glucoseAnnotation']:
            annotations = re.split(config['delimiter'], d['glucoseAnnotation'])
            for a in annotations:
                if "CGM_CALIBRATION_ERROR" in a:
                    plottingData['MarkerCgmCalibrationErrorX'].append(tempDate)
                    plottingData['MarkerCgmCalibrationErrorY'].append(1)
                    cgmIndex += 1
                    plottingData['cgmValuesX'].append([])
                    plottingData['cgmValuesY'].append([])
                    cgmCalibrationErrorAvailable = True
                if "CGM_CONNECTION_ERROR" in a:
                    plottingData['MarkerCgmConnectionErrorX'].append(tempDate)
                    plottingData['MarkerCgmConnectionErrorY'].append(1)
                    cgmIndex += 1
                    plottingData['cgmValuesX'].append([])
                    plottingData['cgmValuesY'].append([])
                    cgmConnectionErrorAvailable = True
                if "CGM_SENSOR_FINISHED" in a:
                    plottingData['MarkerCgmSensorFinishedX'].append(tempDate)
                    plottingData['MarkerCgmSensorFinishedY'].append(1)
                    cgmIndex += 1
                    plottingData['cgmValuesX'].append([])
                    plottingData['cgmValuesY'].append([])
                    cgmSensorFinishedAvailable = True
                if "CGM_SENSOR_START" in a:
                    plottingData['MarkerCgmSensorStartX'].append(tempDate)
                    plottingData['MarkerCgmSensorStartY'].append(1)
                    cgmSensorStartAvailable = True
                if "CGM_TIME_SYNC" in a:
                    plottingData['MarkerCgmTimeSyncX'].append(tempDate)
                    plottingData['MarkerCgmTimeSyncY'].append(1)
                    cgmTimeSyncAvailable = True
                if "GLUCOSE_CGM_CALIBRATION" in a and not "CGM_CALIBRATION_ERROR" in a:
                    plottingData['cgmCalibrationValuesX'].append(tempDate)
                    plottingData['cgmCalibrationValuesY'].append(re.split("=", a)[1])
                if "GLUCOSE_ELEVATION_30" in a:
                    plottingData['glucoseElevationValuesX'].append(tempDate)
                    plottingData['glucoseElevationValuesY'].append(re.split("=", a)[1])
                    plottingData['glucoseElevationValuesCGM'].append(d['cgmValue'])
        if d['bolusCalculationValue']:
            plottingData['bolusCalculationValuesX'].append(tempDate)
            plottingData['bolusCalculationValuesY'].append(d['bolusCalculationValue'])
        if d['heartRateValue']:
            if prevHrDate and ((tempDate - prevHrDate) > datetime.timedelta(minutes=config['interruptLinePlotMinutes'])):
                hrIndex += 1
                plottingData['heartRateValuesX'].append([])
                plottingData['heartRateValuesY'].append([])
            prevHrDate = tempDate
            plottingData['heartRateValuesX'][hrIndex].append(tempDate)
            plottingData['heartRateValuesY'][hrIndex].append(d['heartRateValue'])
        if d['pumpAnnotation']:
            if "PUMP_FILL" in d['pumpAnnotation']:
                plottingData['pumpRewindX'].append(tempDate)
                plottingData['pumpRewindY'].append(1)
                rewindAvailable = True
            if "PUMP_KATERR" in d['pumpAnnotation']:
                plottingData['pumpKatErrX'].append(tempDate)
                plottingData['pumpKatErrY'].append(1)
                katErrAvailable = True
            if "PUMP_RESERVOIR_EMPTY" in d['pumpAnnotation']:
                plottingData['MarkerPumpReservoirEmptyX'].append(tempDate)
                plottingData['MarkerPumpReservoirEmptyY'].append(1)
                pumpReservoirEmptyAvailable = True
            if "PUMP_AUTONOMOUS_SUSPEND" in d['pumpAnnotation']:
                suspendTemp = tempDate
            if "PUMP_UNSUSPEND" in d['pumpAnnotation']:
                if suspendTemp:
                    plottingData['suspendTouples'].append((suspendTemp,tempDate))
                    suspendTemp = []
        if d['exerciseTimeValue']:
            if d['exerciseAnnotation']:
                doAppend = False
                annotations = re.split(config['delimiter'], d['exerciseAnnotation'])
                if "EXERCISE_LOW" in annotations:
                    plottingData['exerciseColor'].append(config['exerciseLowColor'])
                    doAppend = True
                elif "EXERCISE_MID" in annotations:
                    plottingData['exerciseColor'].append(config['exerciseMidColor'])
                    doAppend = True
                elif "EXERCISE_HIGH" in annotations:
                    plottingData['exerciseColor'].append(config['exerciseHighColor'])
                    doAppend = True
                if doAppend:
                    plottingData['exerciseX'].append(tempDate)
                    plottingData['exerciseY'].append(d['exerciseTimeValue'])
        elif d['exerciseAnnotation']:
            plottingData['MarkerExerciseX'].append(tempDate)
            plottingData['MarkerExerciseY'].append(1)
            exerciseAvailable = True
        if d['stressValue']:
            plottingData['stressX'].append(tempDate)
            plottingData['stressY'].append(d['stressValue'])
        if d['locationAnnotation']:
            plottingData['locationX'].append(tempDate)
            plottingData['locationY'].append(d['locationAnnotation'])
        if d['mlCgmValue']:
            tempValue = ""
            if len(re.split(":", d['mlCgmValue'])) <= config['mlCgmArrayIndex']:
                tempValue = re.split(":", d['mlCgmValue'])[0]
            else:
                tempValue = re.split(":", d['mlCgmValue'])[config['mlCgmArrayIndex']]
            if prevCgmMlDate and ((tempDate - prevCgmMlDate) > datetime.timedelta(minutes=config['interruptLinePlotMinutes'])):
                cgmMlIndex += 1
                plottingData['mlCgmValuesX'].append([])
                plottingData['mlCgmValuesY'].append([])
            prevCgmMlDate = tempDate
            plottingData['mlCgmValuesX'][cgmMlIndex].append(tempDate)
            plottingData['mlCgmValuesY'][cgmMlIndex].append(tempValue)
        if d['pumpCgmPredictionValue']:
            if prevCgmMlDate and ((tempDate - prevCgmMlDate) > datetime.timedelta(minutes=config['interruptLinePlotMinutes'])):
                pumpCgmPredictionIndex += 1
                plottingData['pumpCgmPredictionValuesX'].append([])
                plottingData['pumpCgmPredictionValuesY'].append([])
            prevCgmMlDate = tempDate
            plottingData['pumpCgmPredictionValuesX'][pumpCgmPredictionIndex].append(tempDate)
            plottingData['pumpCgmPredictionValuesY'][pumpCgmPredictionIndex].append(d['pumpCgmPredictionValue'])
        if d['otherAnnotation']:
            annotations = re.split(config['delimiter'], d['otherAnnotation'])
            for a in annotations:
                if re.split("=", a)[0] == "USER_TEXT":
                    noteLength = 0
                    for s in re.split("=", a)[1].split():
                        noteLength += len(s) + 1
                        if noteLength > config['maxLengthNotes']:
                            plottingData['noteAnnotation'] += "..."
                            break
                        else:
                            plottingData['noteAnnotation'] += " "
                            plottingData['noteAnnotation'] += s

    # extend basal to the end of the plot
    if plottingData['basalValuesY']:
        plottingData['basalValuesX'].append(endDate)
        plottingData['basalValuesY'].append(plottingData['basalValuesY'][len(plottingData['basalValuesY']) - 1])

    ### Merge barplot values ###
    mergeTimespan = 15
    dateI = beginDate
    currentMerge = 0.0
    currentCount = 0
    for i in range(0, 96):
        ## merge bolus ##
        for i, t in enumerate(bolusValuesX):
            if t >= dateI and t < (dateI + datetime.timedelta(minutes=mergeTimespan)):
                currentMerge += float(bolusValuesY[i])
                currentCount += 1
        if currentCount > 0:
            plottingData['mergedBolusX'].append(dates.date2num(dateI))
            plottingData['mergedBolusY'].append(currentMerge / currentCount)
        currentMerge = 0.0
        currentCount = 0

        ## merge carb ##
        for i, t in enumerate(carbValuesX):
            if t >= dateI and t < (dateI + datetime.timedelta(minutes=mergeTimespan)):
                currentMerge += float(carbValuesY[i])
                currentCount += 1
        if currentCount > 0:
            plottingData['mergedCarbX'].append(dates.date2num(dateI))
            plottingData['mergedCarbY'].append(currentMerge / currentCount)
        currentMerge = 0.0
        currentCount = 0
        dateI = dateI + datetime.timedelta(minutes=mergeTimespan)


    ### daily notes calcs ###
    plottingData['cgmValuesTotal'] = 0
    plottingData['cgmValuesBelow'] = 0
    plottingData['cgmValuesAbove'] = 0
    plottingData['cgmValuesInRange'] = 0
    for i in range(0, len(plottingData['cgmValuesX'])):
        plottingData['cgmValuesTotal'] += len(plottingData['cgmValuesX'][i])
        for j in range(0, len(plottingData['cgmValuesX'][i])):
            if float(plottingData['cgmValuesY'][i][j]) > config['cgmBgLimitMarkerHigh']:
                plottingData['cgmValuesAbove'] += 1
            if float(plottingData['cgmValuesY'][i][j]) < config['cgmBgLimitMarkerLow']:
                plottingData['cgmValuesBelow'] += 1
            if float(plottingData['cgmValuesY'][i][j]) > config['hmin'] and float(plottingData['cgmValuesY'][i][j]) < config['hmax']:
                plottingData['cgmValuesInRange'] += 1
    for t in plottingData['suspendTouples']:
        plottingData['autonomousSuspensionTotal'] += (t[1] - t[0])

    # basal last step
    if prevBasalValue and prevBasalDate:
        plottingData['basalTotal'] += (((endDate - prevBasalDate + datetime.timedelta(seconds=1)).total_seconds() / 3600) * float(prevBasalValue))

    return plottingData

dayCounter = {}
plotCounter = {"SLICE_DAILYSTATISTICS":0, "SLICE_DAILY":0, "SLICE_TINY":0, "SLICE_NORMAL":0, "SLICE_BIG":0}

# plotType: might be SLICE_TINY, SLICE_NORMAL, SLICE_BIG or SLICE_DAILY

def plot(dataset, config, outPath, beginDate, duration, plotType, extLegend, limits, dailyNotes):
    plt.close()
    global dayCounter
    global plotCounter

    datasubset = dataSubset(dataset, beginDate, duration)
    endDate = beginDate + datetime.timedelta(minutes=duration) - datetime.timedelta(seconds=1.0)
    plottingData = prepareDataset(datasubset, config, beginDate, endDate)

    if config['plotElevation']:
        ### glucose elevation ###
        elevationSliceCount = 12
        if plotType == "SLICE_DAILY" or plotType == "SLICE_BIG":
            elevationSliceCount = 12
        elif plotType == "SlICE_NORMAL":
            elevationSliceCount = 6
        elif plotType == "SLICE_TINY":
            elevationSliceCount = 4
        elevationSliceTimespan = (endDate-beginDate) / elevationSliceCount

        elevationSliceCounter = 0
        elevationTempDate = beginDate

        elevationSlices = []
        elevationSlicesCGM = []
        elevationSlicesMax = []
        elevationSlicesMaxCGM = []
        for i in range(0,elevationSliceCount):
            elevationSlices.append([])
            elevationSlicesCGM.append([])
            elevationSlicesMax.append("")
            elevationSlicesMaxCGM.append("")
        for i in range(0,len(plottingData['glucoseElevationValuesX'])):
            while not (plottingData['glucoseElevationValuesX'][i] >= elevationTempDate and plottingData['glucoseElevationValuesX'][i] <= (elevationTempDate+elevationSliceTimespan)):
                elevationSliceCounter += 1
                elevationTempDate += elevationSliceTimespan
            elevationSlices[elevationSliceCounter].append((plottingData['glucoseElevationValuesX'][i],plottingData['glucoseElevationValuesY'][i]))
            elevationSlicesCGM[elevationSliceCounter].append(plottingData['glucoseElevationValuesCGM'][i])

        for i in range(0,len(elevationSlices)):
            if len(elevationSlices[i]) > 0:
                maxDate = elevationSlices[i][0][0]
                maxElevationValue = float(elevationSlices[i][0][1])
                maxCGMValue = float(elevationSlicesCGM[i][0])
                for j in range(1,len(elevationSlices[i])):
                    if maxElevationValue < float(elevationSlices[i][j][1]):
                        maxElevationValue = float(elevationSlices[i][j][1])
                        maxDate = elevationSlices[i][j][0]
                        maxCGMValue = float(elevationSlicesCGM[i][j])
                    elevationSlicesMaxCGM[i] = float(elevationSlicesCGM[i][j])
                middleValue = maxElevationValue
                for d in dataSubset(dataset, maxDate - datetime.timedelta(minutes = 30), 30.0):
                    if d['cgmValue']:
                        middleValue = (maxCGMValue + float(d['cgmValue'])) / 2
                        break
                elevationSlicesMax[i] = (maxElevationValue, middleValue, (maxDate - datetime.timedelta(minutes = 15)))
        for i in range(1,len(elevationSlicesMax)):
            if elevationSlicesMax[i-1] != "" and elevationSlicesMax[i] != "":
                if elevationSlicesMax[i-1][2] < (beginDate + datetime.timedelta(minutes=20)):
                    elevationSlicesMax[i-1] = (elevationSlicesMax[i-1][0], elevationSlicesMax[i-1][1], (beginDate + datetime.timedelta(minutes=20)))
                if elevationSlicesMax[i][2] > (endDate - datetime.timedelta(minutes=20)):
                    elevationSlicesMax[i] = (elevationSlicesMax[i][0], elevationSlicesMax[i][1], (endDate - datetime.timedelta(minutes=20)))
                if (elevationSlicesMax[i][2] - elevationSlicesMax[i-1][2]) <= datetime.timedelta(minutes = 30):
                    if elevationSlicesMax[i-1][0] > elevationSlicesMax[i][0]:
                        elevationSlicesMax[i] = ""
                    else:
                        elevationSlicesMax[i-1] = ""

    ########## Prepare Canvas ##########
    plt.rcParams.update({'font.size': 12})
    plt.rcParams.update({'font.sans-serif': ['Verdana']})
    plt.rcParams.update({'font.family': 'sans-serif'})
    fig = plt.figure()

    axBarPlots = plt.subplot2grid((1000, 1), (620, 0), rowspan=380, colspan=1)
    axSleep = plt.subplot2grid((1000, 1), (144, 0), rowspan=476, colspan=1, sharex=axBarPlots)
    axStress = plt.subplot2grid((1000, 1), (108, 0), rowspan=36, colspan=1, sharex=axBarPlots)
    axExercise = plt.subplot2grid((1000, 1), (72, 0), rowspan=36, colspan=1, sharex=axBarPlots)
    axLocation = plt.subplot2grid((1000, 1), (36, 0), rowspan=36, colspan=1, sharex=axBarPlots)
    axSymbols = plt.subplot2grid((1000, 1), (0, 0), rowspan=36, colspan=1, sharex=axBarPlots)

    if config['plotBasal']:
        axBasal = axBarPlots.twinx()
    axCgmBg = axSleep.twinx()

    # hiding xticks and labels
    for tic in axSleep.xaxis.get_major_ticks():
        tic.tick1On = tic.tick2On = False
        tic.label1On = tic.label2On = False
    for tic in axSymbols.xaxis.get_major_ticks():
        tic.tick1On = tic.tick2On = False
        tic.label1On = tic.label2On = False
    for tic in axStress.xaxis.get_major_ticks():
        tic.tick1On = tic.tick2On = False
        tic.label1On = tic.label2On = False
    for tic in axExercise.xaxis.get_major_ticks():
        tic.tick1On = tic.tick2On = False
        tic.label1On = tic.label2On = False
    for tic in axLocation.xaxis.get_major_ticks():
        tic.tick1On = tic.tick2On = False
        tic.label1On = tic.label2On = False

    axSymbols.yaxis.set_visible(False)
    axExercise.yaxis.set_visible(False)
    axLocation.yaxis.set_visible(False)
    axStress.yaxis.set_visible(False)

    axBarPlots.grid(color=config['gridColor'], linestyle='-', zorder=1)
    axBarPlots.set_axisbelow(True)
    axSleep.grid(color=config['gridColor'], linestyle='-', which='both')
    #axBgCgm.set_axisbelow(True)
    axSymbols.grid(color=config['gridColor'], linestyle='-')
    axSymbols.set_axisbelow(True)

    axBarPlots.xaxis_date()
    axBarPlots.xaxis.set_major_formatter(dates.DateFormatter('%H:%M'))
    axBarPlots.xaxis.set_major_locator(dates.HourLocator(interval=2))

    al = 7
    arrowprops = dict(clip_on=True, headlength=al, headwidth=al, facecolor='k')
    kwargs = dict(xycoords='axes fraction', textcoords='offset points', arrowprops=arrowprops)

    if (config['showXaxisLabel']):
        axBarPlots.set_xlabel(config['xaxisLabel'])
        axBarPlots.xaxis.set_label_position('bottom')

    axSymbols.annotate("", (0, 1), xytext=(0, -al), **kwargs)
    axSymbols.annotate("", (1, 1), xytext=(0, -al), **kwargs)

    # delete space between the subplots to make it look like one
    fig.subplots_adjust(hspace=0)
    ########## Prepare Canvas ##########


    ########## Basal, Bolus, Carb Plot ##########
    if config['plotAutonomousSuspend']:
        for d in plottingData['suspendTouples']:
            axBarPlots.axvspan(d[0], d[1], facecolor=config['autonomousSuspendColor'], zorder=0)
    if config['plotBasal']:
        gridSteps = 6
        barStepSize = 0.5

        if limits['maxBarValue'] >= 6:
            barStepSize = round((limits['maxBarValue']/gridSteps)*2)/2
            if barStepSize * gridSteps < limits['maxBarValue']:
                gridSteps += 1
        elif limits['maxBarValue'] >= 3:
            barStepSize = 1

        barPlotsMax = gridSteps * barStepSize

        axBarPlots.set_ylim(0, barPlotsMax)
        axBarPlots.yaxis.set_major_locator(MultipleLocator(barStepSize))


        basalStepSize = 0.1

        if limits['maxBasalValue'] > 2.5:
            basalStepSize = round(limits['maxBasalValue']/5)
        elif limits['maxBasalValue'] > 1:
            basalStepSize = 0.5
        elif limits['maxBasalValue'] > 0.5:
            basalStepSize = 0.2

        basalMax = gridSteps * basalStepSize

        axBasal.set_ylim(0, basalMax)
        axBasal.yaxis.set_major_locator(MultipleLocator(basalStepSize))
        basalPlot, = axBasal.plot(plottingData['basalValuesX'], plottingData['basalValuesY'], linewidth=config['basalLineWidth'], color=config['basalPlotColor'], drawstyle='steps-post')  # Plot basalValues
        axBasal.set_ylabel(config['basalLabel'])

        axBasal.spines['top'].set_visible(False)

    if config['plotCarb']:
        carbBars = axBarPlots.bar([dates.num2date(t) - datetime.timedelta(minutes=3) for t in plottingData['mergedCarbX']], plottingData['mergedCarbY'], config['barWidth'], color=config['carbBarColor'])
    if config['plotBolus']:
        bolusBars = axBarPlots.bar([dates.num2date(t) + datetime.timedelta(minutes=3) for t in plottingData['mergedBolusX']], plottingData['mergedBolusY'], config['barWidth'], color=config['bolusBarColor'])

        for i in range(0,len(plottingData['bolusSquareValuesX'])):
            tempWidth = dates.date2num(plottingData['bolusSquareValuesX'][i] + datetime.timedelta(seconds=float(plottingData['bolusSquareValuesY'][i][1]))) - dates.date2num(plottingData['bolusSquareValuesX'][i])
            l = axBarPlots.add_patch(
                patches.Rectangle(
                    (dates.date2num(plottingData['bolusSquareValuesX'][i]), 0),  # (x,y)
                    tempWidth,  # width
                    plottingData['bolusSquareValuesY'][i][0],  # height
                    edgecolor=config['bolusBarColor'],
                    facecolor=config['bolusBarColor'],
                    alpha=0.7
                )
            )

    if config['plotCarb'] or config['plotBolus']:
        axBarPlots.set_ylabel(config['bolusLabel'])

        axBarPlots.spines['top'].set_visible(False)
        axBarPlots.xaxis.set_ticks_position('bottom')
    ########## Basal, Bolus, Carb Plot ##########




    ########## CGM, BG, HR PLOT ##########
    minCgmBg = int((limits['minCgmBgValue'] / 25)) * 25
    gridOffset = (50 - minCgmBg) / 25
    axSleep.set_ylim(minCgmBg, config['cgmBgHighLimit'])

    # background field specified by hmin/hmax
    # dummy axis for axhspan
    axCgmBg.set_ylim(minCgmBg, config['cgmBgHighLimit'])
    axCgmBg.axes.get_yaxis().set_visible(False)
    # plot sleep data
    if config['plotSleep']:
        for d in plottingData['sleepTouples']:
            axSleep.axvspan(d[0], d[1], facecolor=d[2], alpha=1.0)

    if config['plotCgm'] or config['plotBg']:
        axSleep.axhspan(config['hmin'], config['hmax'], facecolor=config['hbgColor'], alpha=0.5, edgecolor='#8eb496')
        axCgmBg.axhspan(config['cgmBgLimitMarkerLow'], config['cgmBgLimitMarkerLow'],
                        facecolor=config['cgmBgLimitMarkerColor'],
                        alpha=0.5, edgecolor='#000000')
        axCgmBg.axhspan(config['cgmBgLimitMarkerHigh'], config['cgmBgLimitMarkerHigh'],
                        facecolor=config['cgmBgLimitMarkerColor'],
                        alpha=0.5, edgecolor='#000000')
        # turn off ticks where there is no spine
        axSleep.yaxis.set_ticks_position('left')
        axSleep.set_ylabel(config['bgLabel'])

    if config['plotCgm']:
        cgmRedHighValuesX = [[]]
        cgmRedHighValuesY = [[]]
        cgmRedLowValuesX = [[]]
        cgmRedLowValuesY = [[]]

        cgmRedHighIndex = 0
        cgmRedLowIndex = 0

        cgmRedHighLimit = float(config['cgmBgLimitMarkerHigh'])
        cgmRedLowLimit = float(config['cgmBgLimitMarkerLow'])

        for i in range(0,len(plottingData['pumpCgmPredictionValuesX'])):
            pumpCgmPredictionPlot, = axCgmBg.plot(plottingData['pumpCgmPredictionValuesX'][i], plottingData['pumpCgmPredictionValuesY'][i], marker=config['cgmMarker'], markersize=config['cgmAdditionalMarkerSize'],
                                    linewidth=config['pumpCgmPredictionLineWidth'], color=config['pumpCgmPredictionPlotColor'])

        for i in range(0,len(plottingData['cgmValuesX'])):
            cgmPlot, = axCgmBg.plot(plottingData['cgmValuesX'][i], plottingData['cgmValuesY'][i], marker=config['cgmMarker'], markersize=config['cgmMainMarkerSize'],
                            linewidth=config['cgmLineWidth'], color=config['cgmPlotColor'])  # Plot cgmValues

            prevPoint = (0.0,0.0)

            for j in range(0, len(plottingData['cgmValuesY'][i])):
                ### HIGH LIMIT ###
                if ((float(plottingData['cgmValuesY'][i][j]) > cgmRedHighLimit and float(prevPoint[1]) <= cgmRedHighLimit) or (float(plottingData['cgmValuesY'][i][j]) < cgmRedHighLimit and float(prevPoint[1]) >= cgmRedHighLimit)) and prevPoint[0] != 0.0:
                    x1 = dates.date2num(prevPoint[0])
                    y1 = float(prevPoint[1])
                    x2 = dates.date2num(plottingData['cgmValuesX'][i][j])
                    y2 = float(plottingData['cgmValuesY'][i][j])

                    slope, intercept = linregressScipy([x1, x2], [y1, y2])

                    cgmRedHighValuesX[cgmRedHighIndex].append(dates.num2date(np.linalg.solve([[slope]], [cgmRedHighLimit - intercept])[0]))
                    cgmRedHighValuesY[cgmRedHighIndex].append(cgmRedHighLimit)
                if float(plottingData['cgmValuesY'][i][j]) > cgmRedHighLimit:
                    cgmRedHighValuesX[cgmRedHighIndex].append(plottingData['cgmValuesX'][i][j])
                    cgmRedHighValuesY[cgmRedHighIndex].append(plottingData['cgmValuesY'][i][j])
                if float(plottingData['cgmValuesY'][i][j]) < cgmRedHighLimit and float(prevPoint[1]) >= cgmRedHighLimit:
                    cgmRedHighValuesX.append([])
                    cgmRedHighValuesY.append([])
                    cgmRedHighIndex += 1

                ### LOW LIMIT ###
                if ((float(plottingData['cgmValuesY'][i][j]) <= cgmRedLowLimit and float(prevPoint[1]) > cgmRedLowLimit) or (float(plottingData['cgmValuesY'][i][j]) >= cgmRedLowLimit and float(prevPoint[1]) < cgmRedLowLimit)) and prevPoint[0] != 0.0:
                    x1 = dates.date2num(prevPoint[0])
                    y1 = float(prevPoint[1])
                    x2 = dates.date2num(plottingData['cgmValuesX'][i][j])
                    y2 = float(plottingData['cgmValuesY'][i][j])

                    slope, intercept = linregressScipy([x1, x2], [y1, y2])

                    cgmRedLowValuesX[cgmRedLowIndex].append(dates.num2date(np.linalg.solve([[slope]], [cgmRedLowLimit - intercept])[0]))
                    cgmRedLowValuesY[cgmRedLowIndex].append(cgmRedLowLimit)
                if float(plottingData['cgmValuesY'][i][j]) <= cgmRedLowLimit:
                    cgmRedLowValuesX[cgmRedLowIndex].append(plottingData['cgmValuesX'][i][j])
                    cgmRedLowValuesY[cgmRedLowIndex].append(plottingData['cgmValuesY'][i][j])
                if float(plottingData['cgmValuesY'][i][j]) >= cgmRedLowLimit and float(prevPoint[1]) < cgmRedLowLimit:
                    cgmRedLowValuesX.append([])
                    cgmRedLowValuesY.append([])
                    cgmRedLowIndex += 1

                prevPoint = (plottingData['cgmValuesX'][i][j], plottingData['cgmValuesY'][i][j])

            cgmRedHighValuesX.append([])
            cgmRedHighValuesY.append([])
            cgmRedHighIndex += 1
            cgmRedLowValuesX.append([])
            cgmRedLowValuesY.append([])
            cgmRedLowIndex += 1

        for i in range(0, len(cgmRedHighValuesX)):
            cgmRedHighPlot, = axCgmBg.plot(cgmRedHighValuesX[i], cgmRedHighValuesY[i], marker=config['cgmMarker'],
                                           markersize=config['cgmMainMarkerSize'],
                                           linewidth=config['cgmLineWidth'], color=config['overMaxColor'])  # Plot cgmValues
        for i in range(0, len(cgmRedLowValuesX)):
            cgmRedLowPlot, = axCgmBg.plot(cgmRedLowValuesX[i], cgmRedLowValuesY[i], marker=config['cgmMarker'],
                                           markersize=config['cgmMainMarkerSize'],
                                           linewidth=config['cgmLineWidth'], color=config['overMaxColor'])  # Plot cgmValues

        cgmAlertPlot, = axCgmBg.plot(plottingData['cgmAlertValuesX'], plottingData['cgmAlertValuesY'], 'ro', color=config['cgmPlotColor'],
                                     mec='#000000',
                                     mew=.5)  # Plot cgmAlertValues (datapoints)
        for i in range(0,len(plottingData['mlCgmValuesX'])):
            mlCgmPlot, = axCgmBg.plot(plottingData['mlCgmValuesX'][i], plottingData['mlCgmValuesY'][i], marker=config['cgmMarker'], markersize=config['cgmAdditionalMarkerSize'],
                                    linewidth=config['mlCgmLineWidth'], color=config['mlCgmPlotColor'])

        cgmCalibrationPlot, = axCgmBg.plot(plottingData['cgmCalibrationValuesX'], plottingData['cgmCalibrationValuesY'], config['cgmCalibrationMarker'], fillstyle='none',
                                     mec=config['cgmCalibrationPlotColor'],
                                     mew=2)  # Plot cgmCalibrationValues (datapoints)

        axCgmBg.plot(plottingData['cgmAlertValuesRedX'], plottingData['cgmAlertValuesRedY'], 'ro', color=config['overMaxColor'], mec='#000000', mew=.5)  # Plot cgmAlertValues (datapoints)

    if config['plotBg']:
        bgPlot, = axCgmBg.plot(plottingData['bgValuesX'], plottingData['bgValuesY'], linewidth=config['bgLineWidth'],
                               color=config['bgPlotColor'])  # Plot bgValues
        axCgmBg.plot(plottingData['bgValuesX'], plottingData['bgValuesY'], 'ro', color=config['bgPlotColor'], mec='#000000',
                     mew=.5)  # Plot bgValues (datapoints)

    axSleep.yaxis.set_minor_locator(MultipleLocator(25))
    axSleep.yaxis.set_major_locator(MultipleLocator(50))

    if config['plotBolusCalculation']:
        bolusCalculationPlot, = axCgmBg.plot(plottingData['bolusCalculationValuesX'], plottingData['bolusCalculationValuesY'], marker=config['bolusCalculationMarker'],
                     markersize=config['bolusCalculationMarkerSize'], fillstyle='none', color=config['bolusCalculationColor'], linewidth= 0, mec=config['bolusCalculationColor'],
                     mew=2)  # Plot bgValues (datapoints)
    # heartRate
    if config['plotHeartRate'] and not config['plotCgmRaw']:
        axHeartRate = axSleep.twinx()
        axHeartRate.set_ylabel(config['hrLabel'])

        gridSteps = (10.0 + gridOffset)
        hrStepWidth = round(round((((config['maxHrValue'] - config['minHrValue']) / gridSteps) + 0.49) / 5) + 0.49) * 5

        minHr = (config['minHrValue'] / 10) * 10
        maxHr = minHr + gridSteps * hrStepWidth

        hrTicks = []

        axHeartRate.set_ylim(minHr, maxHr)

        for i in range(0, 11 + gridOffset):
            if i % 2 == 1:
                hrTicks.append(config['minHrValue'] + hrStepWidth * i)
        axHeartRate.set_yticks(hrTicks)

        for i in range(0, len(plottingData['heartRateValuesX'])):
            heartRatePlot, = axHeartRate.plot(plottingData['heartRateValuesX'][i], plottingData['heartRateValuesY'][i], marker=config['heartRateMarker'],
                                          markersize=config['heartRateMarkerSize'],
                                          linewidth=config['heartRateLineWidth'], color=config['heartRatePlotColor'])
        axHeartRate.set_zorder(axSleep.get_zorder() + 1)
    elif config['plotCgmRaw'] and config['plotCgm']:
        axHeartRate = axSleep.twinx()
        axHeartRate.set_ylabel(config['cgmRawLabel'])
        axHeartRate.set_ylim(5, 75)
        for i in range(0, len(plottingData['cgmRawValuesX'])):
            heartRatePlot, = axHeartRate.plot(plottingData['cgmRawValuesX'][i], plottingData['cgmRawValuesY'][i], linewidth=config['cgmRawLineWidth'], color=config['cgmRawPlotColor'])
        axHeartRate.set_zorder(axSleep.get_zorder() + 1)

    # some additional settings
    axSleep.patch.set_visible(False)

    axSleep.spines['bottom'].set_visible(True)
    axSleep.spines['top'].set_visible(False)

    if config['plotElevation']:
        for i in range(0,len(elevationSlices)):
            if elevationSlicesMax[i] and elevationSlicesMaxCGM[i]:
                # arrow style
                arrowStyle = "-|>"
                if float(elevationSlicesMax[i][0]) > 0:
                    arrowStyle = "<|-"
                # y axis index
                #y1 = elevationSlicesMaxCGM[i]
                y1 = elevationSlicesMax[i][1] - 15
                y2 = y1 + 30

                if abs(elevationSlicesMax[i][0]) > config['glucoseElevationN1']:
                    axCgmBg.annotate("", xy=(elevationSlicesMax[i][2] - datetime.timedelta(minutes=13), y1),
                                     xytext=(elevationSlicesMax[i][2] - datetime.timedelta(minutes=13), y2),
                                     arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))
                if abs(elevationSlicesMax[i][0]) > config['glucoseElevationN2']:
                    axCgmBg.annotate("", xy=(elevationSlicesMax[i][2], y1),
                                     xytext=(elevationSlicesMax[i][2], y2),
                                     arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))
                if abs(elevationSlicesMax[i][0]) > config['glucoseElevationN3']:
                    axCgmBg.annotate("", xy=(elevationSlicesMax[i][2] + datetime.timedelta(minutes=13), y1),
                                     xytext=(elevationSlicesMax[i][2] + datetime.timedelta(minutes=13), y2),
                                     arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))

    # z sort subplots
    axCgmBg.set_zorder(axSleep.get_zorder() + 2)

    ########## CGM, BG, HR PLOT ##########



    ########## Symbols ##########
    if config['plotSymbols']:
        pumpRewindPlot, = axSymbols.plot(plottingData['pumpRewindX'], plottingData['pumpRewindY'], config['rewindMarker'], color=config['pumpColor'])
        pumpKatErrPlot, = axSymbols.plot(plottingData['pumpKatErrX'], plottingData['pumpKatErrY'], config['katErrMarker'], color=config['symbolsColor'])
        exercisePlot, = axSymbols.plot(plottingData['MarkerExerciseX'], plottingData['MarkerExerciseY'], config['exerciseMarker'], color=config['symbolsColor'])
        cgmCalibrationErrorPlot, = axSymbols.plot(plottingData['MarkerCgmCalibrationErrorX'], plottingData['MarkerCgmCalibrationErrorY'], config['cgmCalibrationErrorMarker'], color=config['symbolsColor'])
        cgmConnectionErrorPlot, = axSymbols.plot(plottingData['MarkerCgmConnectionErrorX'], plottingData['MarkerCgmConnectionErrorY'], config['cgmConnectionErrorMarker'], color=config['symbolsColor'])
        cgmSensorFinishedPlot, = axSymbols.plot(plottingData['MarkerCgmSensorFinishedX'], plottingData['MarkerCgmSensorFinishedY'], config['cgmSensorFinishedMarker'], color=config['symbolsColor'])
        cgmSensorStartPlot, = axSymbols.plot(plottingData['MarkerCgmSensorStartX'], plottingData['MarkerCgmSensorStartY'], config['cgmSensorStartMarker'], color=config['symbolsColor'])
        cgmTimeSyncPlot, = axSymbols.plot(plottingData['MarkerCgmTimeSyncX'], plottingData['MarkerCgmTimeSyncY'], config['cgmTimeSyncMarker'], color=config['symbolsColor'])
        pumpReservoirEmptyPlot, = axSymbols.plot(plottingData['MarkerPumpReservoirEmptyX'], plottingData['MarkerPumpReservoirEmptyY'], config['pumpReservoirEmptyMarker'], color=config['symbolsColor'])

    axSymbols.spines['bottom'].set_visible(True)
    axSymbols.spines['top'].set_visible(False)

    # background field for the symbols
    axSymbols.axhspan(0.9, 1.1, facecolor=config['symbolsBackgroundColor'], alpha=1, edgecolor='#000000')

    axSymbols.set_ylim([0.9, 1.1])
    ########## Symbols ##########


    ########## Exercise Time Value ##########
    if config['plotExercise']:
        for i in range(0,len(plottingData['exerciseX'])):
            tempWidth = dates.date2num(plottingData['exerciseX'][i] + datetime.timedelta(minutes=float(plottingData['exerciseY'][i]))) - dates.date2num(plottingData['exerciseX'][i])
            axExercise.add_patch(
                patches.Rectangle(
                    (dates.date2num(plottingData['exerciseX'][i]), 0),  # (x,y)
                    tempWidth,  # width
                    1,  # height
                    facecolor=plottingData['exerciseColor'][i],
                    alpha=1.0
                )
            )
    ########## Exercise Time Value ##########

    ########## Stress Level ##########
    if config['plotStress']:
        plottingData['stressX'].append(endDate)
        for i in range(0,len(plottingData['stressX'])-1):
            tempWidth = dates.date2num(plottingData['stressX'][i + 1]) - dates.date2num(plottingData['stressX'][i])
            if float(plottingData['stressY'][i]) == 0.0:
                tempStressColor = "#FFFFFF"
            elif float(plottingData['stressY'][i]) <= 25.0:
                tempStressColor = config['stress1Color']
            elif float(plottingData['stressY'][i]) <= 50.0:
                tempStressColor = config['stress2Color']
            elif float(plottingData['stressY'][i]) <= 75.0:
                tempStressColor = config['stress3Color']
            else:
                tempStressColor = config['stress4Color']

            # pseudo white rect to eliminte potential overlapping
            axStress.add_patch(
                patches.Rectangle(
                    (dates.date2num(plottingData['stressX'][i]), 0),  # (x,y)
                    tempWidth,  # width
                    1,  # height
                    facecolor=tempStressColor,
                    alpha=1.0
                )
            )
    ########## Stress Level ##########


    ########## Location ##########
    if config['plotLocation']:
        plottingData['locationX'].append(endDate)
        for i in range(0,len(plottingData['locationX'])-1):
            tempString = ""
            tempColor = ""
            if plottingData['locationY'][i] == "LOC_TRANSITION":
                tempString = config['locTransitionLabel']
                tempColor = config['locTransitionColor']
            if plottingData['locationY'][i] == "LOC_HOME":
                tempString = config['locHomeLabel']
                tempColor = config['locHomeColor']
            if plottingData['locationY'][i] == "LOC_WORK":
                tempString = config['locWorkLabel']
                tempColor = config['locWorkColor']
            if plottingData['locationY'][i] == "LOC_FOOD":
                tempString = config['locFoodLabel']
                tempColor = config['locFoodColor']
            if plottingData['locationY'][i] == "LOC_SPORTS":
                tempString = config['locSportsLabel']
                tempColor = config['locSportsColor']
            if plottingData['locationY'][i] == "LOC_OTHER":
                tempString = config['locOtherLabel']
                tempColor = config['locOtherColor']

            tempWidth = dates.date2num(plottingData['locationX'][i + 1]) - dates.date2num(plottingData['locationX'][i])
            t = axLocation.text(dates.date2num(plottingData['locationX'][i])+ tempWidth/2, 0.4, tempString, style='italic', color='#000000', alpha=0.5, horizontalalignment='center', verticalalignment='center')
            l = axLocation.add_patch(
                patches.Rectangle(
                    (dates.date2num(plottingData['locationX'][i]), 0),  # (x,y)
                    tempWidth,  # width
                    1,  # height
                    #edgecolor="#000000",
                    facecolor=tempColor
                )
            )
            if (t.get_window_extent(renderer=fig.canvas.get_renderer()).width - 16) > l.get_window_extent(renderer=fig.canvas.get_renderer()).width:
                axLocation.texts.remove(t)
    ########## Location ##########


    ########## Legend ##########
    ## offset calc for legend ##
    axbox = axBarPlots.get_position()
    handles = []
    labels = []
    xOffset = -0.05
    yOffset = 0.29

    if not extLegend:
        if config['plotBg']:
            handles.append(bgPlot)
            labels.append(config['bgLegend'])
        if config['plotCgm']:
            handles.append(cgmPlot)
            labels.append(config['cgmLegend'])
        xOffset = config['legendXOffset']
        yOffset = config['legendYOffset']
    else:
        handles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['autonomousSuspendColor'],
                alpha=0.5
            ))
        labels.append(config['autonomousSuspendLegend'])
    if config['plotBasal']:
        handles.append(basalPlot)
        labels.append(config['basalLegend'])
    if (config['plotBolus'] and len(bolusBars) > 0):
        handles.append(bolusBars)
        labels.append(config['bolusLegend'])
    if (config['plotCarb'] and len(carbBars) > 0):
        handles.append(carbBars)
        labels.append(config['carbLegend'])

    if len(handles) > 0 and plotType == "SLICE_DAILY":
        if (plotCounter[plotType] % 3) == 0 or extLegend:
            dailyLegend = fig.legend(handles, labels, loc="upper left",
                                     bbox_to_anchor=[axbox.x0 + xOffset, axbox.y0 + yOffset], fontsize=10,
                                     edgecolor='#000000')
            dailyLegend.get_frame().set_alpha(0.6)


    ### extended legend ###
    if extLegend:
        axbox2 = axSleep.get_position()
        plotsLegendHandles = []
        plotsLegendLabels = []

        if config['plotBg']:
            plotsLegendHandles.append(bgPlot)
            plotsLegendLabels.append(config['bgLegend'])
        if config['plotCgm']:
            plotsLegendHandles.append(cgmPlot)
            plotsLegendLabels.append(config['cgmLegend'])
            plotsLegendHandles.append(cgmAlertPlot)
            plotsLegendLabels.append(config['cgmAlertLegend'])
            plotsLegendHandles.append(cgmCalibrationPlot)
            plotsLegendLabels.append(config['cgmCalibrationLegend'])
            plotsLegendHandles.append(mlCgmPlot)
            plotsLegendLabels.append(config['mlCgmLegend'])
            plotsLegendHandles.append(pumpCgmPredictionPlot)
            plotsLegendLabels.append(config['pumpCgmPredictionLegend'])
        if config['plotBolusCalculation']:
            plotsLegendHandles.append(bolusCalculationPlot)
            plotsLegendLabels.append(config['bolusCalculatonLegend'])
        if config['plotHeartRate'] and not config['plotCgmRaw']:
            plotsLegendHandles.append(heartRatePlot)
            plotsLegendLabels.append(config['heartRateLegend'])

        if config['plotSleep']:
            plotsLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['deepSleepColor'],
                alpha=1.0
            ))
            plotsLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['lightSleepColor'],
                alpha=1.0
            ))
            plotsLegendLabels.append(config['deepSleepLabel'])
            plotsLegendLabels.append(config['lightSleepLabel'])

        if len(plotsLegendHandles) > 0:
            axbox2 = axSleep.get_position()
            plotsLegend = fig.legend(plotsLegendHandles, plotsLegendLabels, loc="lower left",
                       bbox_to_anchor=[axbox2.x0 + config['legendXOffset'], axbox2.y0], fontsize=10,
                       edgecolor='#000000')
            plotsLegend.get_frame().set_alpha(0.9)


        if config['plotSymbols']:
            rewindPlot = lines.Line2D([], [], marker=config['rewindMarker'], linestyle='None', color=config['symbolsColor'])
            katErrPlot = lines.Line2D([], [], marker=config['katErrMarker'], linestyle='None', color=config['symbolsColor'])
            exercisePlot = lines.Line2D([], [], marker=config['exerciseMarker'], linestyle='None', color=config['symbolsColor'])
            cgmCalibrationErrorPlot = lines.Line2D([], [], marker=config['cgmCalibrationErrorMarker'], linestyle='None', color=config['symbolsColor'])
            cgmConnectionErrorPlot = lines.Line2D([], [], marker=config['cgmConnectionErrorMarker'], linestyle='None', color=config['symbolsColor'])
            cgmSensorFinishedPlot = lines.Line2D([], [], marker=config['cgmSensorFinishedMarker'], linestyle='None', color=config['symbolsColor'])
            cgmSensorStartPlot = lines.Line2D([], [], marker=config['cgmSensorStartMarker'], linestyle='None', color=config['symbolsColor'])
            cgmTimeSyncPlot = lines.Line2D([], [], marker=config['cgmTimeSyncMarker'], linestyle='None', color=config['symbolsColor'])
            pumpReservoirEmptyPlot = lines.Line2D([], [], marker=config['pumpReservoirEmptyMarker'], linestyle='None', color=config['symbolsColor'])

            symbolsLegendHandles = [rewindPlot, katErrPlot, exercisePlot, cgmCalibrationErrorPlot, cgmConnectionErrorPlot, cgmSensorFinishedPlot, cgmSensorStartPlot, cgmTimeSyncPlot, pumpReservoirEmptyPlot]
            symbolsLegendLabels = [config['pumpRewindLegend'], config['pumpKatErrLegend'].replace(' ', '\n'), config['exerciseLegend'], config['cgmCalibrationErrorLegend'], config['cgmConnectionErrorLegend'],
                                   config['cgmSensorFinishedLegend'], config['cgmSensorStartLegend'], config['cgmTimeSyncLegend'], config['pumpReservoirEmptyLegend']]

            symbolsLegend = fig.legend(symbolsLegendHandles, symbolsLegendLabels, loc="upper left",
                       bbox_to_anchor=[axbox2.x0 + config['legendXOffset'] + 0.675, axbox2.y0 + 0.55], fontsize=10,
                       edgecolor='#000000')
            symbolsLegend.get_frame().set_alpha(0.9)

            axSymbols.annotate('', xy=(axbox2.x0 + config['legendXOffset'] + 0.67, 0.4), xycoords='axes fraction',
                               xytext=(axbox2.x0 + config['legendXOffset'] + 0.71, -1.7),
                               arrowprops=dict(arrowstyle='simple', facecolor='black'))
            axSymbols.text(dates.date2num(beginDate) + 0.01, 1, "Symbols", color='#000000',
                           horizontalalignment='left', verticalalignment='center')


        if config['plotLocation']:
            locationLegendHandles = []
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['locNoDataColor'],
                edgecolor="#000000",
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['locTransitionColor'],
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['locHomeColor'],
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['locWorkColor'],
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['locFoodColor'],
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['locSportsColor'],
                alpha=1.0
            ))
            locationLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['locOtherColor'],
                alpha=1.0
            ))

            locationLegendLabels = [config['locNoDataLabel'], config['locTransitionLabel'], config['locHomeLabel'], config['locWorkLabel'], config['locFoodLabel'], config['locSportsLabel'], config['locOtherLabel']]

            axbox2 = axSleep.get_position()
            locationLegend = fig.legend(locationLegendHandles, locationLegendLabels, loc="upper left",
                       bbox_to_anchor=[axbox2.x0 + config['legendXOffset'] + 0.23, axbox2.y0 + 0.55], fontsize=10,
                       edgecolor='#000000')
            locationLegend.get_frame().set_alpha(0.9)

            axSymbols.annotate('', xy=(axbox2.x0 + config['legendXOffset'] + 0.1525, -0.6), xycoords='axes fraction',
                               xytext=(axbox2.x0 + config['legendXOffset'] + 0.1925, -2.7),
                               arrowprops=dict(arrowstyle='simple', facecolor='black'))
            axLocation.text(dates.date2num(beginDate) + 0.01, 0.4, "Location", color='#000000',
                            horizontalalignment='left', verticalalignment='center')

        if config['plotExercise']:
            exerciseLegendHandles = []
            exerciseLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor='#ffffff',
                edgecolor="#000000",
                alpha=1.0
            ))
            exerciseLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['exerciseLowColor'],
                alpha=1.0
            ))
            exerciseLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['exerciseMidColor'],
                alpha=1.0
            ))
            exerciseLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['exerciseHighColor'],
                alpha=1.0
            ))

            exerciseLegendLabels = ["no data", config['exerciseLowLabel'], config['exerciseMidLabel'], config['exerciseHighLabel']]

            axbox2 = axSleep.get_position()
            exerciseLegend = fig.legend(exerciseLegendHandles, exerciseLegendLabels, loc="upper left",
                                        bbox_to_anchor=[axbox2.x0 + config['legendXOffset'] + 0.38, axbox2.y0 + 0.49],
                                        fontsize=10,
                                        edgecolor='#000000')
            exerciseLegend.get_frame().set_alpha(0.9)

            axSymbols.annotate('', xy=(axbox2.x0 + config['legendXOffset'] + 0.327, -1.6), xycoords='axes fraction',
                               xytext=(axbox2.x0 + config['legendXOffset'] + 0.367, -3.7),
                               arrowprops=dict(arrowstyle='simple', facecolor='black'))
            axExercise.text(dates.date2num(beginDate) + 0.01, 0.4, "Exercise", color='#000000',
                            horizontalalignment='left', verticalalignment='center')

        if config['plotStress']:
            stressLegendHandles = []
            stressLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['stress0Color'],
                edgecolor="#000000",
                alpha=1.0
            ))
            stressLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['stress1Color'],
                alpha=1.0
            ))
            stressLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['stress2Color'],
                alpha=1.0
            ))
            stressLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['stress3Color'],
                alpha=1.0
            ))
            stressLegendHandles.append(patches.Rectangle(
                (0, 0),  # (x,y)
                1,  # width
                1,  # height
                facecolor=config['stress4Color'],
                alpha=1.0
            ))

            stressLegendLabels = [config['stress0Label'], config['stress1Label'], config['stress2Label'], config['stress3Label'], config['stress4Label']] # no label for nor data (stress = 0)

            stressLegend = fig.legend(stressLegendHandles, stressLegendLabels, loc="upper left",
                                      bbox_to_anchor=[axbox2.x0 + config['legendXOffset'] + 0.55, axbox2.y0 + 0.46], fontsize=10,
                                      edgecolor='#000000')
            stressLegend.get_frame().set_alpha(0.9)

            axSymbols.annotate('', xy=(axbox2.x0 + config['legendXOffset'] + 0.525, -2.6), xycoords='axes fraction',
                           xytext=(axbox2.x0 + config['legendXOffset'] + 0.565, -4.7),
                           arrowprops=dict(arrowstyle='simple', facecolor='black'))
            axStress.text(dates.date2num(beginDate) + 0.01, 0.4, "Stress", color='#000000',
                        horizontalalignment='left', verticalalignment='center')

            ## elevation legend ##
            placeholder = patches.Rectangle((0, 0), 1, 1, fill=False, edgecolor='none', visible=False)
            elevationLegend = axCgmBg.legend([placeholder, placeholder, placeholder, placeholder, placeholder, placeholder],
                                 [config['lowElevationUpLegend'], config['midElevationUpLegend'], config['highElevationUpLegend'], config['lowElevationDownLegend'], config['midElevationDownLegend'], config['highElevationDownLegend']],
                                 loc="upper left",
                                 bbox_to_anchor=[axbox2.x0 + config['legendXOffset'] + 0.42, axbox2.y0 + 0.126],
                                 fontsize=10, edgecolor='#000000')
            elevationLegend.set_zorder(2)

            elevationLegendTimeOffset = 735
            elevationLegendCGMOffset = 116
            arrowStyle = "<|-"
            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset + 40),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                     elevationLegendCGMOffset + 60),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))

            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset + 20),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                     elevationLegendCGMOffset + 40),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))
            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13), elevationLegendCGMOffset + 20),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13),
                                     elevationLegendCGMOffset + 40),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))

            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                     elevationLegendCGMOffset + 20),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))
            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13), elevationLegendCGMOffset),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13),
                                     elevationLegendCGMOffset + 20),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))
            axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 26), elevationLegendCGMOffset),
                             xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 26),
                                     elevationLegendCGMOffset + 20),
                             arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))

        elevationLegendCGMOffset -= 60
        arrowStyle = "-|>"
        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset + 40),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                 elevationLegendCGMOffset + 60),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))

        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset + 20),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                 elevationLegendCGMOffset + 40),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))
        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13), elevationLegendCGMOffset + 20),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13),
                                 elevationLegendCGMOffset + 40),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))

        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset), elevationLegendCGMOffset),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset),
                                 elevationLegendCGMOffset + 20),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))
        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13), elevationLegendCGMOffset),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 13),
                                 elevationLegendCGMOffset + 20),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))
        axCgmBg.annotate("", xy=(
            beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 26), elevationLegendCGMOffset),
                         xytext=(beginDate + datetime.timedelta(minutes=elevationLegendTimeOffset + 26),
                                 elevationLegendCGMOffset + 20),
                         arrowprops=dict(arrowstyle=arrowStyle, color=config['glucoseElevationColor']))

    ########## Legend ##########


    ########## Generate filename and save fig ##########
    tempDaycounterStr = beginDate.strftime(config['filenameDateFormatString']) + plotType

    if tempDaycounterStr in dayCounter:
        dayCounter[tempDaycounterStr] += 1
    else:
        dayCounter[tempDaycounterStr] = 0

    # filename temp vars
    tempPrefix = config['filenamePrefix']
    tempTimestamp = beginDate.strftime(config['filenameDateFormatString'])
    tempDaycounter = str(dayCounter[tempDaycounterStr])
    tempFileext = config['fileExtension']
    filename = "unnamed.pdf"

    if extLegend:
        filename = config['legendFileDetailed']
    elif plotType == "SLICE_DAILYSTATISTICS":
        filename = tempPrefix + "dailystatistics_" + tempTimestamp + tempFileext
    elif plotType == "SLICE_DAILY":
        filename = tempPrefix + "daily_" + tempTimestamp + tempFileext
    elif plotType == "SLICE_TINY":
        filename = tempPrefix + "tinyslice_" + tempTimestamp + "_" + tempDaycounter + tempFileext
    elif plotType == "SLICE_NORMAL":
        filename = tempPrefix + "normalslice_" + tempTimestamp + "_" + tempDaycounter + tempFileext
    elif plotType == "SLICE_BIG":
        filename = tempPrefix + "bigslice_" + tempTimestamp + "_" + tempDaycounter + tempFileext

    ### canvas fixes
    axBarPlots.set_xlim(beginDate, endDate)
    fig.tight_layout()
    # hide 00:00
    axBarPlots.xaxis.get_major_ticks()[0].label1.set_visible(False)
    # remove overlapping yaxis ticks
    axBarPlots.yaxis.get_major_ticks()[-2].label1.set_visible(False)
    if config['plotBasal']:
        axBasal.yaxis.get_major_ticks()[-2].label2.set_visible(False)


    sliceWidth = 1.0
    ### plotType specific settings
    if plotType == "SLICE_DAILY" or plotType == "SLICE_DAILYSTATISTICS":
        fig.subplots_adjust(hspace=0, top=.99, bottom=.043, right=0.93, left=0.07)
        axSleep.get_yaxis().set_label_coords(-0.06, 0.5)
        if axHeartRate:
            axHeartRate.get_yaxis().set_label_coords(1.06, 0.5)
        axBarPlots.get_yaxis().set_label_coords(-0.06, 0.5)
        axBasal.get_yaxis().set_label_coords(1.06, 0.5)
    elif plotType == "SLICE_TINY":
        axSleep.get_yaxis().set_label_coords(-0.16, 0.5)
        if axHeartRate:
            axHeartRate.get_yaxis().set_label_coords(1.16, 0.5)
        axBarPlots.get_yaxis().set_label_coords(-0.16, 0.5)
        axBasal.get_yaxis().set_label_coords(1.16, 0.5)

        fig.subplots_adjust(hspace=0, top=.99, bottom=.043, right=0.85, left=0.15)
        sliceWidth = 0.395
    elif plotType == "SLICE_NORMAL":
        fig.subplots_adjust(hspace=0, top=.99, bottom=.043, right=0.90, left=0.10)
        sliceWidth = 0.54
    elif plotType == "SLICE_BIG":
        fig.subplots_adjust(hspace=0, top=.99, bottom=.043, right=0.93, left=0.07)
        axSleep.get_yaxis().set_label_coords(-0.06, 0.5)
        if axHeartRate:
            axHeartRate.get_yaxis().set_label_coords(1.06, 0.5)
        axBarPlots.get_yaxis().set_label_coords(-0.06, 0.5)
        axBasal.get_yaxis().set_label_coords(1.06, 0.5)
    plotCounter[plotType] += 1

    scale = 1.5
    tempDailyNotes = ""
    if dailyNotes:
        fig.set_size_inches(6.1 * scale * sliceWidth, 11.69 / 3 * scale)
        tempDailyNotes = generateDailyNotes(config, outPath, plottingData, beginDate)
    else:
        fig.set_size_inches(8.27 * scale * sliceWidth, 11.69 / 3 * scale)

    plt.savefig(os.path.join(outPath, filename))
    plt.clf()
    ########## Generate filename and save fig ##########

    return {'filename':filename, 'dailyNotes':tempDailyNotes}

def generatePlotListTex(config, outPath, slicesInRow, plots, plotListFileName, headerFileName):
    texPlotListSlices = open(os.path.join(outPath, plotListFileName), 'w')
    slicesTableLabels = ""
    slicesTablePlots = ""

    for i in range(0, len(plots)):
        if i % (3*slicesInRow) == 0:
            texPlotListSlices.write("\\newpage\n\\input{" + os.path.splitext(headerFileName)[0]+ "}\n")

        slicesTableLabels += plots[i]['timestamp'].strftime(config['titelDateFormat']) + " &"
        if slicesInRow == 1:
            trim = "0 0 0 0"
        elif slicesInRow == 2:
            if i % slicesInRow == 0:
                trim = "0 0 1.36cm 0"
            elif i % slicesInRow == 1:
                trim = "1.36cm 0 0 0"
        elif slicesInRow == 3:
            if i % slicesInRow == 0:
                trim = "0 0 1.73cm 0"
            elif i % slicesInRow == 1:
                trim = "1.73cm 0 1.73cm 0"
            elif i % slicesInRow == 2:
                trim = "1.73cm 0 0 0"

        slicesTablePlots += "\\includegraphics[scale=0.53,keepaspectratio,trim={" + trim + "},clip]{" + plots[i]['filename'] + "} &"

        if (i % slicesInRow == (slicesInRow - 1)) or i == (len(plots) - 1):
            additionalCols = ""
            if i == (len(plots) - 1):
                for i in range(0,slicesInRow - (i % slicesInRow) - 1):
                    additionalCols += " &"
            texPlotListSlices.write("\\begin{tabularx}{\linewidth}{YYY}\n" + slicesTableLabels[:-2] + additionalCols + "\\\\\n" + slicesTablePlots[:-2] + additionalCols + "\n\\end{tabularx}\n\n")
            slicesTableLabels = ""
            slicesTablePlots = ""

    texPlotListSlices.close()

def generateDailyPlotListWithNotesTex(config, outpath, plots, dailyNotes):
    texPlotListSlices = open(os.path.join(outpath, config['plotListFileDailyStatistics']), 'w')
    slicesTableLabels = ""
    slicesTablePlots = ""

    for i in range(0, len(plots)):
        if i % 3 == 0:
            texPlotListSlices.write("\\newpage\n\\input{" + os.path.splitext(config['headerFileDailyStatistics'])[0]+ "}\n")

        slicesTableLabels += plots[i]['timestamp'].strftime(config['titelDateFormat']) + " &"
        trim = "0 0 0 0"

        if (dailyNotes[i]['basalTotal'] + dailyNotes[i]['bolusTotal']) > 0.0:
            basalTotal = float(
                dailyNotes[i]['basalTotal'] / (dailyNotes[i]['basalTotal'] + dailyNotes[i]['bolusTotal']) * 100)
            bolusTotal = float(
                dailyNotes[i]['bolusTotal'] / (dailyNotes[i]['basalTotal'] + dailyNotes[i]['bolusTotal']) * 100)
        else:
            basalTotal = 0.0
            bolusTotal = 0.0

        slicesTablePlots += "\\includegraphics[scale=0.53,keepaspectratio,trim={" + trim + "},clip,valign=t]{" + plots[i]['filename'] + "} &\n"
        slicesTablePlots += "\\begin{tabular}[t]{ccc}\n"
        slicesTablePlots += "\\includegraphics[scale=0.09,keepaspectratio,valign=t]{" + dailyNotes[i]['timeInRangeFilename'] + "} & \\includegraphics[scale=0.09,keepaspectratio,valign=t]{" + dailyNotes[i]['hypoFilename'] + "} & \\includegraphics[scale=0.09,keepaspectratio,valign=t]{" + dailyNotes[i]['hyperFilename'] + "} \\\\\n"
        slicesTablePlots += config['labelTimeInRange'] + " & " + config['labelHypo'] + " & " + config['labelHyper'] + "\\\\\n"
        slicesTablePlots += "\\\\\n"
        slicesTablePlots += "\\multicolumn{3}{p{2.4in}}{" + config['labelBasalTotal'] + ": " + str('{0:.2f}'.format(dailyNotes[i]['basalTotal'])) + " IE (" + str('{0:.2f}'.format(basalTotal)) + "\\%)} \\\\\n"
        slicesTablePlots += "\\multicolumn{3}{p{2.4in}}{" + config['labelBolusTotal'] + ": " + str('{0:.2f}'.format(dailyNotes[i]['bolusTotal'])) + " IE (" + str('{0:.2f}'.format(bolusTotal)) + "\\%)} \\\\\n"
        slicesTablePlots += "\\multicolumn{3}{p{2.4in}}{" + config['labelCarbTotal'] + ": " + str('{0:.2f}'.format(dailyNotes[i]['carbTotal'])) + " KE} \\\\\n"
        slicesTablePlots += "\\multicolumn{3}{p{2.4in}}{" + config['labelAutonomousSuspentionTime'] + ": " + str(dailyNotes[i]['autonomousSuspensionTotal']) + "h} \\\\\n"
        slicesTablePlots += "\\multicolumn{3}{p{2.4in}}{" + config['labelNote'] + ": " + dailyNotes[i]['noteAnnotation'] + "}\n"
        slicesTablePlots += "\\end{tabular}  "

        additionalCols = ""
        texPlotListSlices.write("\\begin{tabularx}{\linewidth}{ccc}\n" + slicesTableLabels[:-2] + additionalCols + "\\\\\n" + slicesTablePlots[:-2] + additionalCols + "\n\\end{tabularx}\n\n")
        slicesTableLabels = ""
        slicesTablePlots = ""

    texPlotListSlices.close()

def generateDailyPlotListTex(config, outpath, plots):
    generatePlotListTex(config, outpath, 1, plots, config['plotListFileDaily'], config['headerFileDaily'])

def generateTinySlicesPlotListTex(config, outpath, plots):
    generatePlotListTex(config, outpath, 3, plots, config['plotListFileTinySlices'], config['headerFileTinySlices'])

def generateNormalSlicesPlotListTex(config, outpath, plots):
    generatePlotListTex(config, outpath, 2, plots, config['plotListFileNormalSlices'], config['headerFileNormalSlices'])

def generateBigSlicesPlotListTex(config, outpath, plots):
    generatePlotListTex(config, outpath, 1, plots, config['plotListFileBigSlices'], config['headerFileBigSlices'])

def generateDailyNotes(config, outPath, plottingData, beginDate):
    timeInRangeFilename = "timeInRange_" + beginDate.strftime(config['filenameDateFormatString']) + ".png"
    hyperFilename = "hyper_" + beginDate.strftime(config['filenameDateFormatString']) + ".png"
    hypoFilename = "hypo_" + beginDate.strftime(config['filenameDateFormatString']) + ".png"

    fig1 = plt.figure(2)
    ax1 = fig1.add_subplot(111)
    ax1.pie([float(plottingData['cgmValuesInRange']),
             (plottingData['cgmValuesTotal'] - plottingData['cgmValuesInRange'])], explode=(0.0, 0),
            shadow=False, colors=(config['colorTimeInRange'], config['colorNone']), startangle=90)
    ax1.axis('equal')  # Equal aspect ratio ensures that pie is drawn as a circle.
    ax1.text(0, 0, str('{0:.2f}'.format(float(plottingData['cgmValuesInRange']) / float(plottingData['cgmValuesTotal']) * 100)) + "%",
        fontsize=80, horizontalalignment='center', verticalalignment='center')

    plt.subplots_adjust(left=0.0, right=1.0, top=1.04, bottom=-0.04)
    plt.savefig(os.path.join(outPath, timeInRangeFilename))

    ax1.clear()
    ax1.pie([float(plottingData['cgmValuesAbove']),
             (plottingData['cgmValuesTotal'] - float(plottingData['cgmValuesAbove']))], explode=(0.0, 0),
            shadow=False, colors=(config['colorHyper'], config['colorNone']), startangle=90 + (
        float(plottingData['cgmValuesTotal'] - plottingData['cgmValuesBelow'] - plottingData['cgmValuesAbove']) / float(
            plottingData['cgmValuesTotal']) * 360.0))
    ax1.axis('equal')
    ax1.text(0, 0, str('{0:.2f}'.format(float(plottingData['cgmValuesAbove']) / float(plottingData['cgmValuesTotal']) * 100)) + "%",
        fontsize=80, horizontalalignment='center', verticalalignment='center')

    plt.subplots_adjust(left=0.0, right=1.0, top=1.04, bottom=-0.04)
    plt.savefig(os.path.join(outPath, hyperFilename))

    ax1.clear()
    ax1.pie([float(plottingData['cgmValuesBelow']),
             (plottingData['cgmValuesTotal'] - float(plottingData['cgmValuesBelow']))], explode=(0.0, 0),
            shadow=False, colors=(config['colorHypo'], config['colorNone']), startangle=90 + (
        float(plottingData['cgmValuesTotal'] - plottingData['cgmValuesBelow'] - plottingData['cgmValuesAbove']) / float(
            plottingData['cgmValuesTotal']) * 360.0) + (float(plottingData['cgmValuesAbove']) / float(
            plottingData['cgmValuesTotal']) * 360.0))
    ax1.axis('equal')
    ax1.text(0, 0, str('{0:.2f}'.format(float(plottingData['cgmValuesBelow']) / float(plottingData['cgmValuesTotal']) * 100)) + "%",
        fontsize=80, horizontalalignment='center', verticalalignment='center')

    plt.subplots_adjust(left=0.0, right=1.0, top=1.04, bottom=-0.04)
    plt.savefig(os.path.join(outPath, hypoFilename))

    plt.figure(1)
    plt.close(2)

    return {'cgmValuesTotal':float(plottingData['cgmValuesTotal']),
            'cgmValuesAbove':float(plottingData['cgmValuesAbove']),
            'cgmValuesBelow':float(plottingData['cgmValuesBelow']),
            'bolusTotal': float(plottingData['bolusTotal']),
            'basalTotal': float(plottingData['basalTotal']),
            'carbTotal': float(plottingData['carbTotal']),
            'autonomousSuspensionTotal': plottingData['autonomousSuspensionTotal'],
            'timeInRange': float(plottingData['cgmValuesTotal'] - plottingData['cgmValuesBelow']
                                 - plottingData['cgmValuesAbove']),
            'timeInRangeFilename': timeInRangeFilename, 'hypoFilename': hypoFilename, 'hyperFilename': hyperFilename,
            'noteAnnotation': plottingData['noteAnnotation']}


def generateLegendTex(config, outpath, filename):
    fileLegendTex = open(os.path.join(outpath, "tex_g_legendPage.tex"), 'w')
    if filename:
        fileLegendTex.write("\\newpage\n\\centerline{\\huge\\textbf{Legend}}\n\\vspace{2em}\n\\centering{\\includegraphics[width=\\textwidth]{" + filename + "}}\n\\vspace{1em}\n\\input{tex_s_legendText}")
    fileLegendTex.close()

def generateHeaderTex(config, outpath, filename, headLine):
    headerFile = open(os.path.join(outpath, filename), 'w')
    headerFile.write(
        "\\noindent \\large{\\textbf{" + headLine + "}} \\hfill \\small{Page \\thepage/\\pageref{LastPage}}\n\n\\vspace{0.5em}\n")
    headerFile.write(
        "\centerline{\\includegraphics{" + config['legendFileSymbols'] + "}}\n\\vspace{0.1em}")
    headerFile.close()

def generateSymbolsLegend(config, outPath):
    global rewindAvailable
    global katErrAvailable
    global exerciseAvailable
    global cgmCalibrationErrorAvailable
    global cgmConnectionErrorAvailable
    global cgmSensorFinishedAvailable
    global cgmSensorStartAvailable
    global cgmTimeSyncAvailable
    global pumpReservoirEmptyAvailable

    rewindPlot = lines.Line2D([], [], marker=config['rewindMarker'], linestyle='None', color=config['symbolsColor'])
    katErrPlot = lines.Line2D([], [], marker=config['katErrMarker'], linestyle='None', color=config['symbolsColor'])
    exercisePlot = lines.Line2D([], [], marker=config['exerciseMarker'], linestyle='None', color=config['symbolsColor'])
    cgmCalibrationErrorPlot = lines.Line2D([], [], marker=config['cgmCalibrationErrorMarker'], linestyle='None', color=config['symbolsColor'])
    cgmConnectionErrorPlot = lines.Line2D([], [], marker=config['cgmConnectionErrorMarker'], linestyle='None', color=config['symbolsColor'])
    cgmSensorFinishedPlot = lines.Line2D([], [], marker=config['cgmSensorFinishedMarker'], linestyle='None', color=config['symbolsColor'])
    cgmSensorStartPlot = lines.Line2D([], [], marker=config['cgmSensorStartMarker'], linestyle='None', color=config['symbolsColor'])
    cgmTimeSyncPlot = lines.Line2D([], [], marker=config['cgmTimeSyncMarker'], linestyle='None', color=config['symbolsColor'])
    pumpReservoirEmptyPlot = lines.Line2D([], [], marker=config['pumpReservoirEmptyMarker'], linestyle='None', color=config['symbolsColor'])

    handles = []
    labels = []

    if rewindAvailable:
        handles.append(rewindPlot)
        labels.append(config['pumpRewindLegend'])
    if katErrAvailable:
        handles.append(katErrPlot)
        labels.append(config['pumpKatErrLegend'])
    if exerciseAvailable:
        handles.append(exercisePlot)
        labels.append(config['exerciseLegend'])
    if cgmCalibrationErrorAvailable:
        handles.append(cgmCalibrationErrorPlot)
        labels.append(config['cgmCalibrationErrorLegend'])
    if cgmConnectionErrorAvailable:
        handles.append(cgmConnectionErrorPlot)
        labels.append(config['cgmConnectionErrorLegend'])
    if cgmSensorFinishedAvailable:
        handles.append(cgmSensorFinishedPlot)
        labels.append(config['cgmSensorFinishedLegend'])
    if cgmSensorStartAvailable:
        handles.append(cgmSensorStartPlot)
        labels.append(config['cgmSensorStartLegend'])
    if cgmTimeSyncAvailable:
        handles.append(cgmTimeSyncPlot)
        labels.append(config['cgmTimeSyncLegend'])
    if pumpReservoirEmptyAvailable:
        handles.append(pumpReservoirEmptyPlot)
        labels.append(config['pumpReservoirEmptyLegend'])

    figLegend = pylab.figure()
    legend = pylab.figlegend(handles, labels, 'center', numpoints=1, fontsize=6, ncol=4, columnspacing=2)
    figLegend.canvas.draw()

    bbox = legend.get_window_extent().transformed(figLegend.dpi_scale_trans.inverted())
    ll, ur = bbox.get_points()
    x0, y0 = ll
    x1, y1 = ur
    w, h = x1 - x0, y1 - y0
    x1, y1 = x0 + w * 1, y0 + h * 1.1
    bbox = transforms.Bbox(np.array(((x0, y0), (x1, y1))));

    plt.savefig(os.path.join(outPath, config['legendFileSymbols']), bbox_inches=bbox)
    plt.close()

def main():
    versionString = "OpenDiabetesVault-Plot v0.9"
    outPath = os.getcwd()
    csvFileName = ""
    dataset = ""

    parser = OptionParser(usage="%prog [OPTION] [FILE ..]", version=versionString, description=versionString)

    parser.add_option("-f", "--data-set", dest="dataset", metavar="FILE",
                      help="FILE specifies the dataset for the plot.")
    parser.add_option("-c", "--config", dest="config", metavar="FILE", default="config.json",
                      help="FILE specifies configuration file for the plot [Default config.json].")
    parser.add_option("-d", "--daily", dest="daily", action="store_true",
                      help="Activates daily plot.")
    parser.add_option("-s", "--dailystatistics", dest="dailystatistics", action="store_true",
                      help="Activates daily plot with daily statistics.")
    parser.add_option("-t", "--slice-tiny", dest="slicetiny", metavar="FILE",
                      help="FILE specifies slice annotations. Activates slice plot (3 per Line).")
    parser.add_option("-n", "--slice-normal", dest="slicenormal", metavar="FILE",
                      help="FILE specifies slice annotations. Activates slice plot (2 per Line).")
    parser.add_option("-b", "--slice-big", dest="slicebig", metavar="FILE",
                      help="FILE specifies slice annotations. Activates slice plot (1 per Line).")
    parser.add_option("-l", "--legend", dest="legend", action="store_true",
                      help="Activates legend plot.")
    parser.add_option("-o", "--output-path", dest="outputpath", metavar="PATH",
                      help="PATH specifies the output path for generated files. [Default: ./]")

    (options, args) = parser.parse_args()

    if len(sys.argv) == 1:
        parser.print_help()
        exit(0)

    if options.dataset:
        csvFileName = options.dataset
        if not os.path.exists(csvFileName):
            print "[Error] Dataset does not exist"
            exit(0)
    elif options.legend:
        options.slicetiny = False
        options.slicenormal = False
        options.slicebig = False
        options.daily = False
        options.dailystatistics = False
    else:
        print "[Error] No dataset given"
        exit(0)
    if options.config:
        configFileName = options.config
    if options.slicetiny:
        if not os.path.exists(options.slicetiny):
            print "[Error] " + options.slicetiny + " does not exist"
            exit(0)
    if options.slicenormal:
        if not os.path.exists(options.slicenormal):
            print "[Error] " + options.slicenormal + " does not exist"
            exit(0)
    if options.slicebig:
        if not os.path.exists(options.slicebig):
            print "[Error] " + options.slicebig + " does not exist"
            exit(0)
    if options.outputpath:
        outPath = options.outputpath
        if not os.path.exists(outPath):
            print "[Error] Output path does not exist."
            exit(0)

    if not os.path.exists(configFileName):
        print "[Error] Config file \'" + configFileName + "\' does not exist."
        exit(0)


    config = parseConfig(configFileName)

    if csvFileName:
        dataset = parseDataset(csvFileName)
    limits = findLimits(dataset, config)

    numberOfPlots = 0.0
    progressCounter = 0.0

    headLineDailyStatistics = ""
    headLineDaily = ""
    headLineTinySlices = ""
    headLineNormalSlices = ""
    headLineBigSlices = ""


    if options.dailystatistics:
        firstDate = dateParser(dataset[0]['date'], "00:00")
        lastDate = dateParser(dataset[0]['date'], "00:00")
        tempDate = ""
        for d in dataset:
            if d['date'] != tempDate:
                tempDate = d['date']
                numberOfPlots += 1
                if dateParser(tempDate, "00:00") < firstDate:
                    firstDate = dateParser(tempDate, "00:00")
                if dateParser(tempDate, "00:00") > lastDate:
                    lastDate = dateParser(tempDate, "00:00")

        days = str(int(divmod((lastDate - firstDate).total_seconds(), 86400)[0]) + 1)
        headLineDailyStatistics = "Daily Log " + firstDate.strftime('%d.%m.%y') + "-" + lastDate.strftime(
        '%d.%m.%y') + " (" + days + " days)"
    if options.daily:
        firstDate = dateParser(dataset[0]['date'], "00:00")
        lastDate = dateParser(dataset[0]['date'], "00:00")
        tempDate = ""
        for d in dataset:
            if d['date'] != tempDate:
                tempDate = d['date']
                numberOfPlots += 1
                if dateParser(tempDate, "00:00") < firstDate:
                    firstDate = dateParser(tempDate, "00:00")
                if dateParser(tempDate, "00:00") > lastDate:
                    lastDate = dateParser(tempDate, "00:00")

        days = str(int(divmod((lastDate - firstDate).total_seconds(), 86400)[0]) + 1)
        headLineDaily = "Daily Log " + firstDate.strftime('%d.%m.%y') + "-" + lastDate.strftime(
        '%d.%m.%y') + " (" + days + " days)"
    if options.slicetiny:
        datasetTinySlices = parseSlices(options.slicetiny)
        numberOfPlots += len(datasetTinySlices)
        firstDate = dateParser(datasetTinySlices[0]['date'], "00:00")
        lastDate = dateParser(datasetTinySlices[0]['date'], "00:00")
        for s in datasetTinySlices:
            if dateParser(s['date'], "00:00") < firstDate:
                firstDate = dateParser(s['date'], "00:00")
            if dateParser(s['date'], "00:00") > lastDate:
                lastDate = dateParser(s['date'], "00:00")
        headLineTinySlices = "Event Slices (Tiny) " + firstDate.strftime('%d.%m.%y') + "-" + lastDate.strftime(
        '%d.%m.%y')
    if options.slicenormal:
        datasetNormalSlices = parseSlices(options.slicenormal)
        numberOfPlots += len(datasetNormalSlices)
        firstDate = dateParser(datasetNormalSlices[0]['date'], "00:00")
        lastDate = dateParser(datasetNormalSlices[0]['date'], "00:00")
        for s in datasetNormalSlices:
            if dateParser(s['date'], "00:00") < firstDate:
                firstDate = dateParser(s['date'], "00:00")
            if dateParser(s['date'], "00:00") > lastDate:
                lastDate = dateParser(s['date'], "00:00")
        headLineNormalSlices = "Event Slices (Normal) " + firstDate.strftime('%d.%m.%y') + "-" + lastDate.strftime(
        '%d.%m.%y')
    if options.slicebig:
        datasetBigSlices = parseSlices(options.slicebig)
        numberOfPlots += len(datasetBigSlices)
        firstDate = dateParser(datasetBigSlices[0]['date'], "00:00")
        lastDate = dateParser(datasetBigSlices[0]['date'], "00:00")
        for s in datasetBigSlices:
            if dateParser(s['date'], "00:00") < firstDate:
                firstDate = dateParser(s['date'], "00:00")
            if dateParser(s['date'], "00:00") > lastDate:
                lastDate = dateParser(s['date'], "00:00")
        headLineBigSlices = "Event Slices (Big) " + firstDate.strftime('%d.%m.%y') + "-" + lastDate.strftime(
        '%d.%m.%y')
    if options.legend:
        numberOfPlots += 1

    finishedPlotsDailyStatistics = []
    finishedPlotsDaily = []
    finishedNotesDaily = []
    finishedPlotsTinySlices = []
    finishedPlotsNormalSlices = []
    finishedPlotsBigSlices = []
    legendFilename = ""

    print "0.00 %"

    if options.dailystatistics:
        tempDate = ""
        for d in dataset:
            if d['date'] != tempDate:
                tempDate = d['date']
                tempPlot = plot(dataset, config, outPath, dateParser(d['date'], '00:00'), 1440.0, "SLICE_DAILYSTATISTICS", False, limits, True)
                finishedPlotsDailyStatistics.append({'filename': tempPlot['filename'], 'timestamp': dateParser(d['date'], '00:00')})
                finishedNotesDaily.append(tempPlot['dailyNotes'])
                progressCounter += 1
                print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"
    if options.daily:
        tempDate = ""
        for d in dataset:
            if d['date'] != tempDate:
                tempDate = d['date']
                tempPlot = plot(dataset, config, outPath, dateParser(d['date'], '00:00'), 1440.0, "SLICE_DAILY", False, limits, False)
                finishedPlotsDaily.append({'filename': tempPlot['filename'], 'timestamp': dateParser(d['date'], '00:00')})
                finishedNotesDaily.append(tempPlot['dailyNotes'])
                progressCounter += 1
                print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"
    if options.slicetiny:
        for s in datasetTinySlices:
            finishedPlotsTinySlices.append({'filename': plot(dataset, config, outPath, dateParser(s['date'], s['time']), float(s['duration']), "SLICE_TINY", False, limits, False)['filename'], 'timestamp': dateParser(s['date'], s['time'])})
            progressCounter += 1
            print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"
    if options.slicenormal:
        for s in datasetNormalSlices:
            finishedPlotsNormalSlices.append({'filename': plot(dataset, config, outPath, dateParser(s['date'], s['time']), float(s['duration']), "SLICE_NORMAL", False, limits, False)['filename'], 'timestamp': dateParser(s['date'], s['time'])})
            progressCounter += 1
            print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"
    if options.slicebig:
        for s in datasetBigSlices:
            finishedPlotsBigSlices.append({'filename': plot(dataset, config, outPath, dateParser(s['date'], s['time']), float(s['duration']), "SLICE_BIG", False, limits, False)['filename'], 'timestamp': dateParser(s['date'], s['time'])})
            progressCounter += 1
            print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"
    if options.legend:
        legendDataset = parseDataset(absPath("legend-dataset-v10.csv"))
        limits = findLimits(legendDataset, config)
        legendFilename = plot(legendDataset, config, outPath, dateParser(legendDataset[0]['date'], legendDataset[0]['time']), 1440.0, "SLICE_DAILY", True, limits, False)['filename']
        progressCounter += 1
        print str('{0:.2f}'.format(float(progressCounter / numberOfPlots) * 100)) + " %"

    generateSymbolsLegend(config, outPath)

    if options.dailystatistics:
        generateDailyPlotListWithNotesTex(config, outPath, finishedPlotsDailyStatistics, finishedNotesDaily)
    generateDailyPlotListTex(config, outPath, finishedPlotsDaily)
    generateTinySlicesPlotListTex(config, outPath, finishedPlotsTinySlices)
    generateNormalSlicesPlotListTex(config, outPath, finishedPlotsNormalSlices)
    generateBigSlicesPlotListTex(config, outPath, finishedPlotsBigSlices)
    generateLegendTex(config, outPath, legendFilename)

    generateHeaderTex(config, outPath, config['headerFileDailyStatistics'], headLineDailyStatistics)
    generateHeaderTex(config, outPath, config['headerFileDaily'], headLineDaily)
    generateHeaderTex(config, outPath, config['headerFileTinySlices'], headLineTinySlices)
    generateHeaderTex(config, outPath, config['headerFileNormalSlices'], headLineNormalSlices)
    generateHeaderTex(config, outPath, config['headerFileBigSlices'], headLineBigSlices)

    ### copy static files to outPath ###
    if absPathOnly() != outPath:
        shutil.copy2(absPath("report.tex"), outPath)
        shutil.copy2(absPath("tex_s_titlePage.tex"), outPath)
        shutil.copy2(absPath("tex_s_legendText.tex"), outPath)

if __name__ == '__main__':
    main()
