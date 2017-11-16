package de.jhit.opendiabetes.vault.plot.config;

public class PlotteriaConfig {

	// limits
    private boolean limitsManual = false;
    private int hmin = 70;
    private int hmax = 180;
    private double barWidth = 0.004;
    private int bgCgmMaxValue = 300;
    private double maxBasalValue = 0.0;
    private double maxBasalBelowLegendValue = 2.0;
    private int cgmBgHighLimit = 300;
    private int cgmBgLimitMarkerLow = 60;
    private int cgmBgLimitMarkerHigh = 250;
    private double minHrValue = 30.0;
    private double maxHrValue = 170.0;
    private int minCgmBgValue = 50;
    private double maxBarValue = 0.0;
    private double interruptLinePlotMinutes = 20.0;

    private double legendXOffset = -0.05;
    private double legendYOffset = 0.295;

    // Plot booleans
    private boolean statisticsFlag = false;
    private boolean filterBgManual = true;
    private boolean plotCarb = true;
    private boolean plotBolus = true;
    private boolean plotBolusCalculation = true;
    private boolean plotBasal = true;
    private boolean plotBg = true;
    private boolean plotCgm = true;
    private boolean plotCgmRaw = false;
    private boolean plotHeartRate = true;
    private boolean plotSleep = true;
    private boolean plotSymbols = true;
    private boolean plotLocation = true;
    private boolean plotExercise = true;
    private boolean plotStress = true;
    private boolean plotAutonomousSuspend = true;

    // Plot Colors 
    private String hbgColor = "#cbfcd6";
    private String gridColor = "#E6E6E6";
    private String carbBarColor = "#ed1c24";
    private String bolusBarColor = "#177d36";
    private String bolusCalculationColor = "#177d36";
    private String bgPlotColor = "#2f55c2";
    private String cgmPlotColor = "#4ddde0";
    private String cgmRawPlotColor = "#cc3399";
    private String cgmCalibrationPlotColor = "#2f55c2";
    private String mlCgmPlotColor = "#9f24e6";
    private String pumpCgmPredictionPlotColor = "#949196";
    private String basalPlotColor = "#ffa67a";
    private String heartRatePlotColor = "#e5aba3";
    private String overMaxColor = "#FF0000";
    private String symbolsColor = "#000000";
    private String pumpColor = "#000000";
    private String symbolsBackgroundColor = "#dedede";
    private String cgmBgLimitMarkerColor = "#FFFFFF";
    private String stress0Color = "#ffffff";
    private String stress1Color = "#cea6b8";
    private String stress2Color = "#aa7197";
    private String stress3Color = "#80486d";
    private String stress4Color = "#c40234";
    private String exerciseLowColor = "#9ed3f1";
    private String exerciseMidColor = "#32a0de";
    private String exerciseHighColor = "#355f77";
    private String lightSleepColor = "#e1e4e5";
    private String deepSleepColor = "#cfd5d7";
    private String autonomousSuspendColor = "#ffede4";

    // Axis Labels
    private boolean showXaxisLabel = false;
    private String xaxisLabel = "Time of Day [hh:mm]";
    private String bolusLabel = "Bolus/Carb [IE/BE]";
    private String basalLabel = "Basal [IE]";
    private String bgLabel = "BG [mg/dl]";
    private String hrLabel = "Heart Rate [Hz]";
    private String cgmRawLabel = "cgm raw";

    private String titelDateFormat = "%a %d. %b";

    private String delimiter = ":";

    // Legend Labels
    private String bgLegend = "bg";
    private String cgmLegend = "cgm / fgm";
    private String cgmAlertLegend = "cgm alert / fgm read";
    private String cgmCalibrationLegend = "cgm calibration";
    private String mlCgmLegend = "cgm machine learner prediction";
    private String pumpCgmPredictionLegend = "pump cgm prediction";
    private String basalLegend = "basal";
    private String heartRateLegend = "heart rate";
    private String carbLegend = "carb";
    private String bolusLegend = "bolus";
    private String bolusCalculatonLegend = "bolus calculation";
    private String autonomousSuspendLegend = "autonomous suspend";

    // Symbol Labels
    private String exerciseLegend = "exercise";
    private String pumpRewindLegend = "rewind";
    private String pumpKatErrLegend = "insulin blocked";
    private String cgmCalibrationErrorLegend = "cgm/fgm cal err";
    private String cgmConnectionErrorLegend = "cgm/fgm con err";
    private String cgmSensorFinishedLegend = "cgm/fgm expired";
    private String cgmSensorStartLegend = "cgm/fgm start";
    private String cgmTimeSyncLegend = "time sync";
    private String pumpReservoirEmptyLegend = "reservoir empty";

    // Stress Labels
    private String stress0Label = "no data";
    private String stress1Label = "rest";
    private String stress2Label = "low stress";
    private String stress3Label = "mid stress";
    private String stress4Label = "high stress";

    // Markers https://matplotlib.org/api/markers_api.html
    // "."	point
    // ","	pixel
    // "o"	circle
    // "v"	triangle_down
    // "^"	triangle_up
    // "<"	triangle_left
    // ">"	triangle_right
    // "1"	tri_down
    // "2"	tri_up
    // "3"	tri_left
    // "4"	tri_right
    // "8"	octagon
    // "s"	square
    // "p"	pentagon
    // "P"	plus (filled)
    // "*"	star
    // "h"	hexagon1
    // "H"	hexagon2
    // "+"	plus
    // "x"	x
    // "X"	x (filled)
    // "D"	diamond
    // "d"	thin_diamond
    // "|"	vline
    // "_"	hline
    // Symbol Markers
    private String rewindMarker = "<";
    private String katErrMarker = "x";
    private String exerciseMarker = "h";
    private String cgmCalibrationErrorMarker = "^";
    private String cgmConnectionErrorMarker = "v";
    private String cgmSensorFinishedMarker = "|";
    private String cgmSensorStartMarker = ">";
    private String cgmTimeSyncMarker = "8";
    private String pumpReservoirEmptyMarker = "+";

    // Plot Markers
    private String cgmMarker = ".";
    private String bolusCalculationMarker = "s";
    private String heartRateMarker = ".";
    private String cgmCalibrationMarker = "D";
    private int cgmMarkerSize = 5;
    private int bolusCalculationMarkerSize = 5;
    private int heartRateMarkerSize = 2;

    // Locations
    private String locNoDataLabel = "no data";
    private String locTransitionLabel = "transition";
    private String locHomeLabel = "home";
    private String locWorkLabel = "work";
    private String locFoodLabel = "food";
    private String locSportsLabel = "sports";
    private String locOtherLabel = "other";

    private String locNoDataColor = "#ffffff";
    private String locTransitionColor = "#e45d6d";
    private String locHomeColor = "#d5cf3d";
    private String locWorkColor = "#fbc725";
    private String locFoodColor = "#e2b07d"; //9b6131
    private String locSportsColor = "#5086c3";
    private String locOtherColor = "#bfbcbd";

    // Exercise
    private String exerciseLowLabel = "low exercise";
    private String exerciseMidLabel = "mid exercise";
    private String exerciseHighLabel = "high exercise";

    // Sleep
    private String lightSleepLabel = "light sleep";
    private String deepSleepLabel = "deep sleep";

    // Linewidths
    private double heartRateLineWidth = 1.0;
    private double basalLineWidth = 1.0;
    private double cgmLineWidth = 2.5;
    private double cgmRawLineWidth = 1.0;
    private double mlCgmLineWidth = 1.0;
    private double pumpCgmPredictionLineWidth = 1.0;
    private double bgLineWidth = 1.0;
    private double bolusCalculationLineWidth = 1.0;

    // File Settings
    private String fileExtension = ".pdf";

    // TODO hardcode following options in script (but still maintainable)
    // OR be consistend and make it available for all files
    private String filenamePrefix = "plot_";
    private String filenameDateFormatString = "%y%m%d";
    private String plotListFileDaily = "tex_g_plotListDaily.tex";
    private String plotListFileTinySlices = "tex_g_plotListTinySlices.tex";
    private String plotListFileNormalSlices = "tex_g_plotListNormalSlices.tex";
    private String plotListFileBigSlices = "tex_g_plotListBigSlices.tex";
    private String headerFileDaily = "tex_g_headerDaily.tex";
    private String headerFileTinySlices = "tex_g_headerTinySlices.tex";
    private String headerFileNormalSlices = "tex_g_headerNormalSlices.tex";
    private String headerFileBigSlices = "tex_g_headerBigSlices.tex";
    private String legendFileSymbols = "legendSymbols.pdf";
    private String legendFileDetailed = "legendDetailed.pdf";
	public boolean isLimitsManual() {
		return limitsManual;
	}
	public void setLimitsManual(boolean limitsManual) {
		this.limitsManual = limitsManual;
	}
	public int getHmin() {
		return hmin;
	}
	public void setHmin(int hmin) {
		this.hmin = hmin;
	}
	public int getHmax() {
		return hmax;
	}
	public void setHmax(int hmax) {
		this.hmax = hmax;
	}
	public double getBarWidth() {
		return barWidth;
	}
	public void setBarWidth(double barWidth) {
		this.barWidth = barWidth;
	}
	public int getBgCgmMaxValue() {
		return bgCgmMaxValue;
	}
	public void setBgCgmMaxValue(int bgCgmMaxValue) {
		this.bgCgmMaxValue = bgCgmMaxValue;
	}
	public double getMaxBasalValue() {
		return maxBasalValue;
	}
	public void setMaxBasalValue(double maxBasalValue) {
		this.maxBasalValue = maxBasalValue;
	}
	public double getMaxBasalBelowLegendValue() {
		return maxBasalBelowLegendValue;
	}
	public void setMaxBasalBelowLegendValue(double maxBasalBelowLegendValue) {
		this.maxBasalBelowLegendValue = maxBasalBelowLegendValue;
	}
	public int getCgmBgHighLimit() {
		return cgmBgHighLimit;
	}
	public void setCgmBgHighLimit(int cgmBgHighLimit) {
		this.cgmBgHighLimit = cgmBgHighLimit;
	}
	public int getCgmBgLimitMarkerLow() {
		return cgmBgLimitMarkerLow;
	}
	public void setCgmBgLimitMarkerLow(int cgmBgLimitMarkerLow) {
		this.cgmBgLimitMarkerLow = cgmBgLimitMarkerLow;
	}
	public int getCgmBgLimitMarkerHigh() {
		return cgmBgLimitMarkerHigh;
	}
	public void setCgmBgLimitMarkerHigh(int cgmBgLimitMarkerHigh) {
		this.cgmBgLimitMarkerHigh = cgmBgLimitMarkerHigh;
	}
	public double getMinHrValue() {
		return minHrValue;
	}
	public void setMinHrValue(double minHrValue) {
		this.minHrValue = minHrValue;
	}
	public double getMaxHrValue() {
		return maxHrValue;
	}
	public void setMaxHrValue(double maxHrValue) {
		this.maxHrValue = maxHrValue;
	}
	public int getMinCgmBgValue() {
		return minCgmBgValue;
	}
	public void setMinCgmBgValue(int minCgmBgValue) {
		this.minCgmBgValue = minCgmBgValue;
	}
	public double getMaxBarValue() {
		return maxBarValue;
	}
	public void setMaxBarValue(double maxBarValue) {
		this.maxBarValue = maxBarValue;
	}
	public double getInterruptLinePlotMinutes() {
		return interruptLinePlotMinutes;
	}
	public void setInterruptLinePlotMinutes(double interruptLinePlotMinutes) {
		this.interruptLinePlotMinutes = interruptLinePlotMinutes;
	}
	public double getLegendXOffset() {
		return legendXOffset;
	}
	public void setLegendXOffset(double legendXOffset) {
		this.legendXOffset = legendXOffset;
	}
	public double getLegendYOffset() {
		return legendYOffset;
	}
	public void setLegendYOffset(double legendYOffset) {
		this.legendYOffset = legendYOffset;
	}
	public boolean isStatisticsFlag() {
		return statisticsFlag;
	}
	public void setStatisticsFlag(boolean statisticsFlag) {
		this.statisticsFlag = statisticsFlag;
	}
	public boolean isFilterBgManual() {
		return filterBgManual;
	}
	public void setFilterBgManual(boolean filterBgManual) {
		this.filterBgManual = filterBgManual;
	}
	public boolean isPlotCarb() {
		return plotCarb;
	}
	public void setPlotCarb(boolean plotCarb) {
		this.plotCarb = plotCarb;
	}
	public boolean isPlotBolus() {
		return plotBolus;
	}
	public void setPlotBolus(boolean plotBolus) {
		this.plotBolus = plotBolus;
	}
	public boolean isPlotBolusCalculation() {
		return plotBolusCalculation;
	}
	public void setPlotBolusCalculation(boolean plotBolusCalculation) {
		this.plotBolusCalculation = plotBolusCalculation;
	}
	public boolean isPlotBasal() {
		return plotBasal;
	}
	public void setPlotBasal(boolean plotBasal) {
		this.plotBasal = plotBasal;
	}
	public boolean isPlotBg() {
		return plotBg;
	}
	public void setPlotBg(boolean plotBg) {
		this.plotBg = plotBg;
	}
	public boolean isPlotCgm() {
		return plotCgm;
	}
	public void setPlotCgm(boolean plotCgm) {
		this.plotCgm = plotCgm;
	}
	public boolean isPlotCgmRaw() {
		return plotCgmRaw;
	}
	public void setPlotCgmRaw(boolean plotCgmRaw) {
		this.plotCgmRaw = plotCgmRaw;
	}
	public boolean isPlotHeartRate() {
		return plotHeartRate;
	}
	public void setPlotHeartRate(boolean plotHeartRate) {
		this.plotHeartRate = plotHeartRate;
	}
	public boolean isPlotSleep() {
		return plotSleep;
	}
	public void setPlotSleep(boolean plotSleep) {
		this.plotSleep = plotSleep;
	}
	public boolean isPlotSymbols() {
		return plotSymbols;
	}
	public void setPlotSymbols(boolean plotSymbols) {
		this.plotSymbols = plotSymbols;
	}
	public boolean isPlotLocation() {
		return plotLocation;
	}
	public void setPlotLocation(boolean plotLocation) {
		this.plotLocation = plotLocation;
	}
	public boolean isPlotExercise() {
		return plotExercise;
	}
	public void setPlotExercise(boolean plotExercise) {
		this.plotExercise = plotExercise;
	}
	public boolean isPlotStress() {
		return plotStress;
	}
	public void setPlotStress(boolean plotStress) {
		this.plotStress = plotStress;
	}
	public boolean isPlotAutonomousSuspend() {
		return plotAutonomousSuspend;
	}
	public void setPlotAutonomousSuspend(boolean plotAutonomousSuspend) {
		this.plotAutonomousSuspend = plotAutonomousSuspend;
	}
	public String getHbgColor() {
		return hbgColor;
	}
	public void setHbgColor(String hbgColor) {
		this.hbgColor = hbgColor;
	}
	public String getGridColor() {
		return gridColor;
	}
	public void setGridColor(String gridColor) {
		this.gridColor = gridColor;
	}
	public String getCarbBarColor() {
		return carbBarColor;
	}
	public void setCarbBarColor(String carbBarColor) {
		this.carbBarColor = carbBarColor;
	}
	public String getBolusBarColor() {
		return bolusBarColor;
	}
	public void setBolusBarColor(String bolusBarColor) {
		this.bolusBarColor = bolusBarColor;
	}
	public String getBolusCalculationColor() {
		return bolusCalculationColor;
	}
	public void setBolusCalculationColor(String bolusCalculationColor) {
		this.bolusCalculationColor = bolusCalculationColor;
	}
	public String getBgPlotColor() {
		return bgPlotColor;
	}
	public void setBgPlotColor(String bgPlotColor) {
		this.bgPlotColor = bgPlotColor;
	}
	public String getCgmPlotColor() {
		return cgmPlotColor;
	}
	public void setCgmPlotColor(String cgmPlotColor) {
		this.cgmPlotColor = cgmPlotColor;
	}
	public String getCgmRawPlotColor() {
		return cgmRawPlotColor;
	}
	public void setCgmRawPlotColor(String cgmRawPlotColor) {
		this.cgmRawPlotColor = cgmRawPlotColor;
	}
	public String getCgmCalibrationPlotColor() {
		return cgmCalibrationPlotColor;
	}
	public void setCgmCalibrationPlotColor(String cgmCalibrationPlotColor) {
		this.cgmCalibrationPlotColor = cgmCalibrationPlotColor;
	}
	public String getMlCgmPlotColor() {
		return mlCgmPlotColor;
	}
	public void setMlCgmPlotColor(String mlCgmPlotColor) {
		this.mlCgmPlotColor = mlCgmPlotColor;
	}
	public String getPumpCgmPredictionPlotColor() {
		return pumpCgmPredictionPlotColor;
	}
	public void setPumpCgmPredictionPlotColor(String pumpCgmPredictionPlotColor) {
		this.pumpCgmPredictionPlotColor = pumpCgmPredictionPlotColor;
	}
	public String getBasalPlotColor() {
		return basalPlotColor;
	}
	public void setBasalPlotColor(String basalPlotColor) {
		this.basalPlotColor = basalPlotColor;
	}
	public String getHeartRatePlotColor() {
		return heartRatePlotColor;
	}
	public void setHeartRatePlotColor(String heartRatePlotColor) {
		this.heartRatePlotColor = heartRatePlotColor;
	}
	public String getOverMaxColor() {
		return overMaxColor;
	}
	public void setOverMaxColor(String overMaxColor) {
		this.overMaxColor = overMaxColor;
	}
	public String getSymbolsColor() {
		return symbolsColor;
	}
	public void setSymbolsColor(String symbolsColor) {
		this.symbolsColor = symbolsColor;
	}
	public String getPumpColor() {
		return pumpColor;
	}
	public void setPumpColor(String pumpColor) {
		this.pumpColor = pumpColor;
	}
	public String getSymbolsBackgroundColor() {
		return symbolsBackgroundColor;
	}
	public void setSymbolsBackgroundColor(String symbolsBackgroundColor) {
		this.symbolsBackgroundColor = symbolsBackgroundColor;
	}
	public String getCgmBgLimitMarkerColor() {
		return cgmBgLimitMarkerColor;
	}
	public void setCgmBgLimitMarkerColor(String cgmBgLimitMarkerColor) {
		this.cgmBgLimitMarkerColor = cgmBgLimitMarkerColor;
	}
	public String getStress0Color() {
		return stress0Color;
	}
	public void setStress0Color(String stress0Color) {
		this.stress0Color = stress0Color;
	}
	public String getStress1Color() {
		return stress1Color;
	}
	public void setStress1Color(String stress1Color) {
		this.stress1Color = stress1Color;
	}
	public String getStress2Color() {
		return stress2Color;
	}
	public void setStress2Color(String stress2Color) {
		this.stress2Color = stress2Color;
	}
	public String getStress3Color() {
		return stress3Color;
	}
	public void setStress3Color(String stress3Color) {
		this.stress3Color = stress3Color;
	}
	public String getStress4Color() {
		return stress4Color;
	}
	public void setStress4Color(String stress4Color) {
		this.stress4Color = stress4Color;
	}
	public String getExerciseLowColor() {
		return exerciseLowColor;
	}
	public void setExerciseLowColor(String exerciseLowColor) {
		this.exerciseLowColor = exerciseLowColor;
	}
	public String getExerciseMidColor() {
		return exerciseMidColor;
	}
	public void setExerciseMidColor(String exerciseMidColor) {
		this.exerciseMidColor = exerciseMidColor;
	}
	public String getExerciseHighColor() {
		return exerciseHighColor;
	}
	public void setExerciseHighColor(String exerciseHighColor) {
		this.exerciseHighColor = exerciseHighColor;
	}
	public String getLightSleepColor() {
		return lightSleepColor;
	}
	public void setLightSleepColor(String lightSleepColor) {
		this.lightSleepColor = lightSleepColor;
	}
	public String getDeepSleepColor() {
		return deepSleepColor;
	}
	public void setDeepSleepColor(String deepSleepColor) {
		this.deepSleepColor = deepSleepColor;
	}
	public String getAutonomousSuspendColor() {
		return autonomousSuspendColor;
	}
	public void setAutonomousSuspendColor(String autonomousSuspendColor) {
		this.autonomousSuspendColor = autonomousSuspendColor;
	}
	public boolean isShowXaxisLabel() {
		return showXaxisLabel;
	}
	public void setShowXaxisLabel(boolean showXaxisLabel) {
		this.showXaxisLabel = showXaxisLabel;
	}
	public String getXaxisLabel() {
		return xaxisLabel;
	}
	public void setXaxisLabel(String xaxisLabel) {
		this.xaxisLabel = xaxisLabel;
	}
	public String getBolusLabel() {
		return bolusLabel;
	}
	public void setBolusLabel(String bolusLabel) {
		this.bolusLabel = bolusLabel;
	}
	public String getBasalLabel() {
		return basalLabel;
	}
	public void setBasalLabel(String basalLabel) {
		this.basalLabel = basalLabel;
	}
	public String getBgLabel() {
		return bgLabel;
	}
	public void setBgLabel(String bgLabel) {
		this.bgLabel = bgLabel;
	}
	public String getHrLabel() {
		return hrLabel;
	}
	public void setHrLabel(String hrLabel) {
		this.hrLabel = hrLabel;
	}
	public String getCgmRawLabel() {
		return cgmRawLabel;
	}
	public void setCgmRawLabel(String cgmRawLabel) {
		this.cgmRawLabel = cgmRawLabel;
	}
	public String getTitelDateFormat() {
		return titelDateFormat;
	}
	public void setTitelDateFormat(String titelDateFormat) {
		this.titelDateFormat = titelDateFormat;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public String getBgLegend() {
		return bgLegend;
	}
	public void setBgLegend(String bgLegend) {
		this.bgLegend = bgLegend;
	}
	public String getCgmLegend() {
		return cgmLegend;
	}
	public void setCgmLegend(String cgmLegend) {
		this.cgmLegend = cgmLegend;
	}
	public String getCgmAlertLegend() {
		return cgmAlertLegend;
	}
	public void setCgmAlertLegend(String cgmAlertLegend) {
		this.cgmAlertLegend = cgmAlertLegend;
	}
	public String getCgmCalibrationLegend() {
		return cgmCalibrationLegend;
	}
	public void setCgmCalibrationLegend(String cgmCalibrationLegend) {
		this.cgmCalibrationLegend = cgmCalibrationLegend;
	}
	public String getMlCgmLegend() {
		return mlCgmLegend;
	}
	public void setMlCgmLegend(String mlCgmLegend) {
		this.mlCgmLegend = mlCgmLegend;
	}
	public String getPumpCgmPredictionLegend() {
		return pumpCgmPredictionLegend;
	}
	public void setPumpCgmPredictionLegend(String pumpCgmPredictionLegend) {
		this.pumpCgmPredictionLegend = pumpCgmPredictionLegend;
	}
	public String getBasalLegend() {
		return basalLegend;
	}
	public void setBasalLegend(String basalLegend) {
		this.basalLegend = basalLegend;
	}
	public String getHeartRateLegend() {
		return heartRateLegend;
	}
	public void setHeartRateLegend(String heartRateLegend) {
		this.heartRateLegend = heartRateLegend;
	}
	public String getCarbLegend() {
		return carbLegend;
	}
	public void setCarbLegend(String carbLegend) {
		this.carbLegend = carbLegend;
	}
	public String getBolusLegend() {
		return bolusLegend;
	}
	public void setBolusLegend(String bolusLegend) {
		this.bolusLegend = bolusLegend;
	}
	public String getBolusCalculatonLegend() {
		return bolusCalculatonLegend;
	}
	public void setBolusCalculatonLegend(String bolusCalculatonLegend) {
		this.bolusCalculatonLegend = bolusCalculatonLegend;
	}
	public String getAutonomousSuspendLegend() {
		return autonomousSuspendLegend;
	}
	public void setAutonomousSuspendLegend(String autonomousSuspendLegend) {
		this.autonomousSuspendLegend = autonomousSuspendLegend;
	}
	public String getExerciseLegend() {
		return exerciseLegend;
	}
	public void setExerciseLegend(String exerciseLegend) {
		this.exerciseLegend = exerciseLegend;
	}
	public String getPumpRewindLegend() {
		return pumpRewindLegend;
	}
	public void setPumpRewindLegend(String pumpRewindLegend) {
		this.pumpRewindLegend = pumpRewindLegend;
	}
	public String getPumpKatErrLegend() {
		return pumpKatErrLegend;
	}
	public void setPumpKatErrLegend(String pumpKatErrLegend) {
		this.pumpKatErrLegend = pumpKatErrLegend;
	}
	public String getCgmCalibrationErrorLegend() {
		return cgmCalibrationErrorLegend;
	}
	public void setCgmCalibrationErrorLegend(String cgmCalibrationErrorLegend) {
		this.cgmCalibrationErrorLegend = cgmCalibrationErrorLegend;
	}
	public String getCgmConnectionErrorLegend() {
		return cgmConnectionErrorLegend;
	}
	public void setCgmConnectionErrorLegend(String cgmConnectionErrorLegend) {
		this.cgmConnectionErrorLegend = cgmConnectionErrorLegend;
	}
	public String getCgmSensorFinishedLegend() {
		return cgmSensorFinishedLegend;
	}
	public void setCgmSensorFinishedLegend(String cgmSensorFinishedLegend) {
		this.cgmSensorFinishedLegend = cgmSensorFinishedLegend;
	}
	public String getCgmSensorStartLegend() {
		return cgmSensorStartLegend;
	}
	public void setCgmSensorStartLegend(String cgmSensorStartLegend) {
		this.cgmSensorStartLegend = cgmSensorStartLegend;
	}
	public String getCgmTimeSyncLegend() {
		return cgmTimeSyncLegend;
	}
	public void setCgmTimeSyncLegend(String cgmTimeSyncLegend) {
		this.cgmTimeSyncLegend = cgmTimeSyncLegend;
	}
	public String getPumpReservoirEmptyLegend() {
		return pumpReservoirEmptyLegend;
	}
	public void setPumpReservoirEmptyLegend(String pumpReservoirEmptyLegend) {
		this.pumpReservoirEmptyLegend = pumpReservoirEmptyLegend;
	}
	public String getStress0Label() {
		return stress0Label;
	}
	public void setStress0Label(String stress0Label) {
		this.stress0Label = stress0Label;
	}
	public String getStress1Label() {
		return stress1Label;
	}
	public void setStress1Label(String stress1Label) {
		this.stress1Label = stress1Label;
	}
	public String getStress2Label() {
		return stress2Label;
	}
	public void setStress2Label(String stress2Label) {
		this.stress2Label = stress2Label;
	}
	public String getStress3Label() {
		return stress3Label;
	}
	public void setStress3Label(String stress3Label) {
		this.stress3Label = stress3Label;
	}
	public String getStress4Label() {
		return stress4Label;
	}
	public void setStress4Label(String stress4Label) {
		this.stress4Label = stress4Label;
	}
	public String getRewindMarker() {
		return rewindMarker;
	}
	public void setRewindMarker(String rewindMarker) {
		this.rewindMarker = rewindMarker;
	}
	public String getKatErrMarker() {
		return katErrMarker;
	}
	public void setKatErrMarker(String katErrMarker) {
		this.katErrMarker = katErrMarker;
	}
	public String getExerciseMarker() {
		return exerciseMarker;
	}
	public void setExerciseMarker(String exerciseMarker) {
		this.exerciseMarker = exerciseMarker;
	}
	public String getCgmCalibrationErrorMarker() {
		return cgmCalibrationErrorMarker;
	}
	public void setCgmCalibrationErrorMarker(String cgmCalibrationErrorMarker) {
		this.cgmCalibrationErrorMarker = cgmCalibrationErrorMarker;
	}
	public String getCgmConnectionErrorMarker() {
		return cgmConnectionErrorMarker;
	}
	public void setCgmConnectionErrorMarker(String cgmConnectionErrorMarker) {
		this.cgmConnectionErrorMarker = cgmConnectionErrorMarker;
	}
	public String getCgmSensorFinishedMarker() {
		return cgmSensorFinishedMarker;
	}
	public void setCgmSensorFinishedMarker(String cgmSensorFinishedMarker) {
		this.cgmSensorFinishedMarker = cgmSensorFinishedMarker;
	}
	public String getCgmSensorStartMarker() {
		return cgmSensorStartMarker;
	}
	public void setCgmSensorStartMarker(String cgmSensorStartMarker) {
		this.cgmSensorStartMarker = cgmSensorStartMarker;
	}
	public String getCgmTimeSyncMarker() {
		return cgmTimeSyncMarker;
	}
	public void setCgmTimeSyncMarker(String cgmTimeSyncMarker) {
		this.cgmTimeSyncMarker = cgmTimeSyncMarker;
	}
	public String getPumpReservoirEmptyMarker() {
		return pumpReservoirEmptyMarker;
	}
	public void setPumpReservoirEmptyMarker(String pumpReservoirEmptyMarker) {
		this.pumpReservoirEmptyMarker = pumpReservoirEmptyMarker;
	}
	public String getCgmMarker() {
		return cgmMarker;
	}
	public void setCgmMarker(String cgmMarker) {
		this.cgmMarker = cgmMarker;
	}
	public String getBolusCalculationMarker() {
		return bolusCalculationMarker;
	}
	public void setBolusCalculationMarker(String bolusCalculationMarker) {
		this.bolusCalculationMarker = bolusCalculationMarker;
	}
	public String getHeartRateMarker() {
		return heartRateMarker;
	}
	public void setHeartRateMarker(String heartRateMarker) {
		this.heartRateMarker = heartRateMarker;
	}
	public String getCgmCalibrationMarker() {
		return cgmCalibrationMarker;
	}
	public void setCgmCalibrationMarker(String cgmCalibrationMarker) {
		this.cgmCalibrationMarker = cgmCalibrationMarker;
	}
	public int getCgmMarkerSize() {
		return cgmMarkerSize;
	}
	public void setCgmMarkerSize(int cgmMarkerSize) {
		this.cgmMarkerSize = cgmMarkerSize;
	}
	public int getBolusCalculationMarkerSize() {
		return bolusCalculationMarkerSize;
	}
	public void setBolusCalculationMarkerSize(int bolusCalculationMarkerSize) {
		this.bolusCalculationMarkerSize = bolusCalculationMarkerSize;
	}
	public int getHeartRateMarkerSize() {
		return heartRateMarkerSize;
	}
	public void setHeartRateMarkerSize(int heartRateMarkerSize) {
		this.heartRateMarkerSize = heartRateMarkerSize;
	}
	public String getLocNoDataLabel() {
		return locNoDataLabel;
	}
	public void setLocNoDataLabel(String locNoDataLabel) {
		this.locNoDataLabel = locNoDataLabel;
	}
	public String getLocTransitionLabel() {
		return locTransitionLabel;
	}
	public void setLocTransitionLabel(String locTransitionLabel) {
		this.locTransitionLabel = locTransitionLabel;
	}
	public String getLocHomeLabel() {
		return locHomeLabel;
	}
	public void setLocHomeLabel(String locHomeLabel) {
		this.locHomeLabel = locHomeLabel;
	}
	public String getLocWorkLabel() {
		return locWorkLabel;
	}
	public void setLocWorkLabel(String locWorkLabel) {
		this.locWorkLabel = locWorkLabel;
	}
	public String getLocFoodLabel() {
		return locFoodLabel;
	}
	public void setLocFoodLabel(String locFoodLabel) {
		this.locFoodLabel = locFoodLabel;
	}
	public String getLocSportsLabel() {
		return locSportsLabel;
	}
	public void setLocSportsLabel(String locSportsLabel) {
		this.locSportsLabel = locSportsLabel;
	}
	public String getLocOtherLabel() {
		return locOtherLabel;
	}
	public void setLocOtherLabel(String locOtherLabel) {
		this.locOtherLabel = locOtherLabel;
	}
	public String getLocNoDataColor() {
		return locNoDataColor;
	}
	public void setLocNoDataColor(String locNoDataColor) {
		this.locNoDataColor = locNoDataColor;
	}
	public String getLocTransitionColor() {
		return locTransitionColor;
	}
	public void setLocTransitionColor(String locTransitionColor) {
		this.locTransitionColor = locTransitionColor;
	}
	public String getLocHomeColor() {
		return locHomeColor;
	}
	public void setLocHomeColor(String locHomeColor) {
		this.locHomeColor = locHomeColor;
	}
	public String getLocWorkColor() {
		return locWorkColor;
	}
	public void setLocWorkColor(String locWorkColor) {
		this.locWorkColor = locWorkColor;
	}
	public String getLocFoodColor() {
		return locFoodColor;
	}
	public void setLocFoodColor(String locFoodColor) {
		this.locFoodColor = locFoodColor;
	}
	public String getLocSportsColor() {
		return locSportsColor;
	}
	public void setLocSportsColor(String locSportsColor) {
		this.locSportsColor = locSportsColor;
	}
	public String getLocOtherColor() {
		return locOtherColor;
	}
	public void setLocOtherColor(String locOtherColor) {
		this.locOtherColor = locOtherColor;
	}
	public String getExerciseLowLabel() {
		return exerciseLowLabel;
	}
	public void setExerciseLowLabel(String exerciseLowLabel) {
		this.exerciseLowLabel = exerciseLowLabel;
	}
	public String getExerciseMidLabel() {
		return exerciseMidLabel;
	}
	public void setExerciseMidLabel(String exerciseMidLabel) {
		this.exerciseMidLabel = exerciseMidLabel;
	}
	public String getExerciseHighLabel() {
		return exerciseHighLabel;
	}
	public void setExerciseHighLabel(String exerciseHighLabel) {
		this.exerciseHighLabel = exerciseHighLabel;
	}
	public String getLightSleepLabel() {
		return lightSleepLabel;
	}
	public void setLightSleepLabel(String lightSleepLabel) {
		this.lightSleepLabel = lightSleepLabel;
	}
	public String getDeepSleepLabel() {
		return deepSleepLabel;
	}
	public void setDeepSleepLabel(String deepSleepLabel) {
		this.deepSleepLabel = deepSleepLabel;
	}
	public double getHeartRateLineWidth() {
		return heartRateLineWidth;
	}
	public void setHeartRateLineWidth(double heartRateLineWidth) {
		this.heartRateLineWidth = heartRateLineWidth;
	}
	public double getBasalLineWidth() {
		return basalLineWidth;
	}
	public void setBasalLineWidth(double basalLineWidth) {
		this.basalLineWidth = basalLineWidth;
	}
	public double getCgmLineWidth() {
		return cgmLineWidth;
	}
	public void setCgmLineWidth(double cgmLineWidth) {
		this.cgmLineWidth = cgmLineWidth;
	}
	public double getCgmRawLineWidth() {
		return cgmRawLineWidth;
	}
	public void setCgmRawLineWidth(double cgmRawLineWidth) {
		this.cgmRawLineWidth = cgmRawLineWidth;
	}
	public double getMlCgmLineWidth() {
		return mlCgmLineWidth;
	}
	public void setMlCgmLineWidth(double mlCgmLineWidth) {
		this.mlCgmLineWidth = mlCgmLineWidth;
	}
	public double getPumpCgmPredictionLineWidth() {
		return pumpCgmPredictionLineWidth;
	}
	public void setPumpCgmPredictionLineWidth(double pumpCgmPredictionLineWidth) {
		this.pumpCgmPredictionLineWidth = pumpCgmPredictionLineWidth;
	}
	public double getBgLineWidth() {
		return bgLineWidth;
	}
	public void setBgLineWidth(double bgLineWidth) {
		this.bgLineWidth = bgLineWidth;
	}
	public double getBolusCalculationLineWidth() {
		return bolusCalculationLineWidth;
	}
	public void setBolusCalculationLineWidth(double bolusCalculationLineWidth) {
		this.bolusCalculationLineWidth = bolusCalculationLineWidth;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public String getFilenamePrefix() {
		return filenamePrefix;
	}
	public void setFilenamePrefix(String filenamePrefix) {
		this.filenamePrefix = filenamePrefix;
	}
	public String getFilenameDateFormatString() {
		return filenameDateFormatString;
	}
	public void setFilenameDateFormatString(String filenameDateFormatString) {
		this.filenameDateFormatString = filenameDateFormatString;
	}
	public String getPlotListFileDaily() {
		return plotListFileDaily;
	}
	public void setPlotListFileDaily(String plotListFileDaily) {
		this.plotListFileDaily = plotListFileDaily;
	}
	public String getPlotListFileTinySlices() {
		return plotListFileTinySlices;
	}
	public void setPlotListFileTinySlices(String plotListFileTinySlices) {
		this.plotListFileTinySlices = plotListFileTinySlices;
	}
	public String getPlotListFileNormalSlices() {
		return plotListFileNormalSlices;
	}
	public void setPlotListFileNormalSlices(String plotListFileNormalSlices) {
		this.plotListFileNormalSlices = plotListFileNormalSlices;
	}
	public String getPlotListFileBigSlices() {
		return plotListFileBigSlices;
	}
	public void setPlotListFileBigSlices(String plotListFileBigSlices) {
		this.plotListFileBigSlices = plotListFileBigSlices;
	}
	public String getHeaderFileDaily() {
		return headerFileDaily;
	}
	public void setHeaderFileDaily(String headerFileDaily) {
		this.headerFileDaily = headerFileDaily;
	}
	public String getHeaderFileTinySlices() {
		return headerFileTinySlices;
	}
	public void setHeaderFileTinySlices(String headerFileTinySlices) {
		this.headerFileTinySlices = headerFileTinySlices;
	}
	public String getHeaderFileNormalSlices() {
		return headerFileNormalSlices;
	}
	public void setHeaderFileNormalSlices(String headerFileNormalSlices) {
		this.headerFileNormalSlices = headerFileNormalSlices;
	}
	public String getHeaderFileBigSlices() {
		return headerFileBigSlices;
	}
	public void setHeaderFileBigSlices(String headerFileBigSlices) {
		this.headerFileBigSlices = headerFileBigSlices;
	}
	public String getLegendFileSymbols() {
		return legendFileSymbols;
	}
	public void setLegendFileSymbols(String legendFileSymbols) {
		this.legendFileSymbols = legendFileSymbols;
	}
	public String getLegendFileDetailed() {
		return legendFileDetailed;
	}
	public void setLegendFileDetailed(String legendFileDetailed) {
		this.legendFileDetailed = legendFileDetailed;
	}
}
