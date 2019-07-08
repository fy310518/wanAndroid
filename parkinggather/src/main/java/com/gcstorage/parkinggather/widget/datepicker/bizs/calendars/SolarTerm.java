package com.gcstorage.parkinggather.widget.datepicker.bizs.calendars;


import com.gcstorage.parkinggather.widget.datepicker.utils.DataUtils;

/**
 * 农历二十四节气算法
 * 该算法目前仅对外提供一个接口方法获取某年的节气数据{@link #buildSolarTerm(int)}
 *
 * Algorithm of lunar solar term.
 * This algorithm provide a public method to get the all solar term of year.{@link #buildSolarTerm(int)}
 *
 * @author AigeStudio 2015-06-16
 */
final class SolarTerm {
    private static final double E10[] = {1.75347045673, 0.00000000000, 0.0000000000, 0.03341656456, 4.66925680417, 6283.0758499914, 0.00034894275, 4.62610241759, 12566.1516999828, 0.00003417571, 2.82886579606, 3.5231183490, 0.00003497056, 2.74411800971, 5753.3848848968, 0.00003135896, 3.62767041758, 77713.7714681205, 0.00002676218, 4.41808351397, 7860.4193924392, 0.00002342687, 6.13516237631, 3930.2096962196, 0.00001273166, 2.03709655772, 529.6909650946, 0.00001324292, 0.74246356352, 11506.7697697936, 0.00000901855, 2.04505443513, 26.2983197998, 0.00001199167, 1.10962944315, 1577.3435424478, 0.00000857223, 3.50849156957, 398.1490034082, 0.00000779786, 1.17882652114, 5223.6939198022, 0.00000990250, 5.23268129594, 5884.9268465832, 0.00000753141, 2.53339053818, 5507.5532386674, 0.00000505264, 4.58292563052, 18849.2275499742, 0.00000492379, 4.20506639861, 775.5226113240, 0.00000356655, 2.91954116867, 0.0673103028, 0.00000284125, 1.89869034186, 796.2980068164, 0.00000242810, 0.34481140906, 5486.7778431750, 0.00000317087, 5.84901952218, 11790.6290886588, 0.00000271039, 0.31488607649, 10977.0788046990, 0.00000206160, 4.80646606059, 2544.3144198834, 0.00000205385, 1.86947813692, 5573.1428014331, 0.00000202261, 2.45767795458, 6069.7767545534, 0.00000126184, 1.08302630210, 20.7753954924, 0.00000155516, 0.83306073807, 213.2990954380, 0.00000115132, 0.64544911683, 0.9803210682, 0.00000102851, 0.63599846727, 4694.0029547076, 0.00000101724, 4.26679821365, 7.1135470008, 0.00000099206, 6.20992940258, 2146.1654164752, 0.00000132212, 3.41118275555, 2942.4634232916, 0.00000097607, 0.68101272270, 155.4203994342, 0.00000085128, 1.29870743025, 6275.9623029906, 0.00000074651, 1.75508916159, 5088.6288397668, 0.00000101895, 0.97569221824, 15720.8387848784, 0.00000084711, 3.67080093025, 71430.6956181291, 0.00000073547, 4.67926565481, 801.8209311238, 0.00000073874, 3.50319443167, 3154.6870848956, 0.00000078756, 3.03698313141, 12036.4607348882, 0.00000079637, 1.80791330700, 17260.1546546904, 0.00000085803, 5.98322631256, 161000.6857376741, 0.00000056963, 2.78430398043, 6286.5989683404, 0.00000061148, 1.81839811024, 7084.8967811152, 0.00000069627, 0.83297596966, 9437.7629348870, 0.00000056116, 4.38694880779, 14143.4952424306, 0.00000062449, 3.97763880587, 8827.3902698748, 0.00000051145, 0.28306864501, 5856.4776591154, 0.00000055577, 3.47006009062, 6279.5527316424, 0.00000041036, 5.36817351402, 8429.2412664666, 0.00000051605, 1.33282746983, 1748.0164130670, 0.00000051992, 0.18914945834, 12139.5535091068, 0.00000049000, 0.48735065033, 1194.4470102246, 0.00000039200, 6.16832995016, 10447.3878396044, 0.00000035566, 1.77597314691, 6812.7668150860, 0.00000036770, 6.04133859347, 10213.2855462110, 0.00000036596, 2.56955238628, 1059.3819301892, 0.00000033291, 0.59309499459, 17789.8456197850, 0.00000035954, 1.70876111898, 2352.8661537718};

    private static final double E11[] = {6283.31966747491, 0.00000000000, 0.0000000000, 0.00206058863, 2.67823455584, 6283.0758499914, 0.00004303430, 2.63512650414, 12566.1516999828, 0.00000425264, 1.59046980729, 3.5231183490, 0.00000108977, 2.96618001993, 1577.3435424478, 0.00000093478, 2.59212835365, 18849.2275499742, 0.00000119261, 5.79557487799, 26.2983197998, 0.00000072122, 1.13846158196, 529.6909650946, 0.00000067768, 1.87472304791, 398.1490034082, 0.00000067327, 4.40918235168, 5507.5532386674, 0.00000059027, 2.88797038460, 5223.6939198022, 0.00000055976, 2.17471680261, 155.4203994342, 0.00000045407, 0.39803079805, 796.2980068164, 0.00000036369, 0.46624739835, 775.5226113240, 0.00000028958, 2.64707383882, 7.1135470008, 0.00000019097, 1.84628332577, 5486.7778431750, 0.00000020844, 5.34138275149, 0.9803210682, 0.00000018508, 4.96855124577, 213.2990954380, 0.00000016233, 0.03216483047, 2544.3144198834, 0.00000017293, 2.99116864949, 6275.9623029906};

    private static final double E12[] = {0.00052918870, 0.00000000000, 0.0000000000, 0.00008719837, 1.07209665242, 6283.0758499914, 0.00000309125, 0.86728818832, 12566.1516999828, 0.00000027339, 0.05297871691, 3.5231183490, 0.00000016334, 5.18826691036, 26.2983197998, 0.00000015752, 3.68457889430, 155.4203994342, 0.00000009541, 0.75742297675, 18849.2275499742, 0.00000008937, 2.05705419118, 77713.7714681205, 0.00000006952, 0.82673305410, 775.5226113240, 0.00000005064, 4.66284525271, 1577.3435424478};

    private static final double E13[] = {0.00000289226, 5.84384198723, 6283.0758499914, 0.00000034955, 0.00000000000, 0.0000000000, 0.00000016819, 5.48766912348, 12566.1516999828};

    private static final double E14[] = {0.00000114084, 3.14159265359, 0.0000000000, 0.00000007717, 4.13446589358, 6283.0758499914, 0.00000000765, 3.83803776214, 12566.1516999828};

    private static final double E15[] = {0.00000000878, 3.14159265359, 0.0000000000};

    private static final double E20[] = {0.00000279620, 3.19870156017, 84334.6615813083, 0.00000101643, 5.42248619256, 5507.5532386674, 0.00000080445, 3.88013204458, 5223.6939198022, 0.00000043806, 3.70444689758, 2352.8661537718, 0.00000031933, 4.00026369781, 1577.3435424478, 0.00000022724, 3.98473831560, 1047.7473117547, 0.00000016392, 3.56456119782, 5856.4776591154, 0.00000018141, 4.98367470263, 6283.0758499914, 0.00000014443, 3.70275614914, 9437.7629348870, 0.00000014304, 3.41117857525, 10213.2855462110};

    private static final double E21[] = {0.00000009030, 3.89729061890, 5507.5532386674, 0.00000006177, 1.73038850355, 5223.6939198022};

    private static final double E30[] = {1.00013988799, 0.00000000000, 0.0000000000, 0.01670699626, 3.09846350771, 6283.0758499914, 0.00013956023, 3.05524609620, 12566.1516999828, 0.00003083720, 5.19846674381, 77713.7714681205, 0.00001628461, 1.17387749012, 5753.3848848968, 0.00001575568, 2.84685245825, 7860.4193924392, 0.00000924799, 5.45292234084, 11506.7697697936, 0.00000542444, 4.56409149777, 3930.2096962196};

    private static final double E31[] = {0.00103018608, 1.10748969588, 6283.0758499914, 0.00001721238, 1.06442301418, 12566.1516999828, 0.00000702215, 3.14159265359, 0.0000000000};

    private static final double E32[] = {0.00004359385, 5.78455133738, 6283.0758499914};

    private static final double E33[] = {0.00000144595, 4.27319435148, 6283.0758499914};

    private static final double M10[] = {22639.5858800, 2.3555545723, 8328.6914247251, 1.5231275E-04, 2.5041111E-07, -1.1863391E-09, 4586.4383203, 8.0413790709, 7214.0628654588, -2.1850087E-04, -1.8646419E-07, 8.7760973E-10, 2369.9139357, 10.3969336431, 15542.7542901840, -6.6188121E-05, 6.3946925E-08, -3.0872935E-10, 769.0257187, 4.7111091445, 16657.3828494503, 3.0462550E-04, 5.0082223E-07, -2.3726782E-09, -666.4175399, -0.0431256817, 628.3019552485, -2.6638815E-06, 6.1639211E-10, -5.4439728E-11, -411.5957339, 3.2558104895, 16866.9323152810, -1.2804259E-04, -9.8998954E-09, 4.0433461E-11, 211.6555524, 5.6858244986, -1114.6285592663, -3.7081362E-04, -4.3687530E-07, 2.0639488E-09, 205.4359530, 8.0845047526, 6585.7609102104, -2.1583699E-04, -1.8708058E-07, 9.3204945E-10, 191.9561973, 12.7524882154, 23871.4457149091, 8.6124629E-05, 3.1435804E-07, -1.4950684E-09, 164.7286185, 10.4400593249, 14914.4523349355, -6.3524240E-05, 6.3330532E-08, -2.5428962E-10, -147.3213842, -2.3986802540, -7700.3894694766, -1.5497663E-04, -2.4979472E-07, 1.1318993E-09, -124.9881185, 5.1984668216, 7771.3771450920, -3.3094061E-05, 3.1973462E-08, -1.5436468E-10, -109.3803637, 2.3124288905, 8956.9933799736, 1.4964887E-04, 2.5102751E-07, -1.2407788E-09, 55.1770578, 7.1411231536, -1324.1780250970, 6.1854469E-05, 7.3846820E-08, -3.4916281E-10, -45.0996092, 5.6113650618, 25195.6237400061, 2.4270161E-05, 2.4051122E-07, -1.1459056E-09, 39.5333010, -0.9002559173, -8538.2408905558, 2.8035534E-04, 2.6031101E-07, -1.2267725E-09, 38.4298346, 18.4383127140, 22756.8171556428, -2.8468899E-04, -1.2251727E-07, 5.6888037E-10, 36.1238141, 7.0666637168, 24986.0742741754, 4.5693825E-04, 7.5123334E-07, -3.5590172E-09, 30.7725751, 16.0827581417, 14428.1257309177, -4.3700174E-04, -3.7292838E-07, 1.7552195E-09, -28.3971008, 7.9982533891, 7842.3648207073, -2.2116475E-04, -1.8584780E-07, 8.2317000E-10, -24.3582283, 10.3538079614, 16171.0562454324, -6.8852003E-05, 6.4563317E-08, -3.6316908E-10, -18.5847068, 2.8429122493, -557.3142796331, -1.8540681E-04, -2.1843765E-07, 1.0319744E-09, 17.9544674, 5.1553411398, 8399.6791003405, -3.5757942E-05, 3.2589854E-08, -2.0880440E-10, 14.5302779, 12.7956138971, 23243.1437596606, 8.8788511E-05, 3.1374165E-07, -1.4406287E-09, 14.3796974, 15.1080427876, 32200.1371396342, 2.3843738E-04, 5.6476915E-07, -2.6814075E-09, 14.2514576, -24.0810366320, -2.3011998397, 1.5231275E-04, 2.5041111E-07, -1.1863391E-09, 13.8990596, 20.7938672862, 31085.5085803679, -1.3237624E-04, 1.2789385E-07, -6.1745870E-10, 13.1940636, 3.3302699264, -9443.3199839914, -5.2312637E-04, -6.8728642E-07, 3.2502879E-09, -9.6790568, -4.7542348263, -16029.0808942018, -3.0728938E-04, -5.0020584E-07, 2.3182384E-09, -9.3658635, 11.2971895604, 24080.9951807398, -3.4654346E-04, -1.9636409E-07, 9.1804319E-10, 8.6055318, 5.7289501804, -1742.9305145148, -3.6814974E-04, -4.3749170E-07, 2.1183885E-09, -8.4530982, 7.5540213938, 16100.0685698171, 1.1921869E-04, 2.8238458E-07, -1.3407038E-09, 8.0501724, 10.4831850066, 14286.1503796870, -6.0860358E-05, 6.2714140E-08, -1.9984990E-10, -7.6301553, 4.6679834628, 17285.6848046987, 3.0196162E-04, 5.0143862E-07, -2.4271179E-09, -7.4474952, -0.0862513635, 1256.6039104970, -5.3277630E-06, 1.2327842E-09, -1.0887946E-10, 7.3712011, 8.1276304344, 5957.4589549619, -2.1317311E-04, -1.8769697E-07, 9.8648918E-10, 7.0629900, 0.9591375719, 33.7570471374, -3.0829302E-05, -3.6967043E-08, 1.7385419E-10, -6.3831491, 9.4966777258, 7004.5133996281, 2.1416722E-04, 3.2425793E-07, -1.5355019E-09, -5.7416071, 13.6527441326, 32409.6866054649, -1.9423071E-04, 5.4047029E-08, -2.6829589E-10, 4.3740095, 18.4814383957, 22128.5152003943, -2.8202511E-04, -1.2313366E-07, 6.2332010E-10, -3.9976134, 7.9669196340, 33524.3151647312, 1.7658291E-04, 4.9092233E-07, -2.3322447E-09, -3.2096876, 13.2398458924, 14985.4400105508, -2.5159493E-04, -1.5449073E-07, 7.2324505E-10, -2.9145404, 12.7093625336, 24499.7476701576, 8.3460748E-05, 3.1497443E-07, -1.5495082E-09, 2.7318890, 16.1258838235, 13799.8237756692, -4.3433786E-04, -3.7354477E-07, 1.8096592E-09, -2.5679459, -2.4418059357, -7072.0875142282, -1.5764051E-04, -2.4917833E-07, 1.0774596E-09, -2.5211990, 7.9551277074, 8470.6667759558, -2.2382863E-04, -1.8523141E-07, 7.6873027E-10, 2.4888871, 5.6426988169, -486.3266040178, -3.7347750E-04, -4.3625891E-07, 2.0095091E-09, 2.1460741, 7.1842488353, -1952.4799803455, 6.4518350E-05, 7.3230428E-08, -2.9472308E-10, 1.9777270, 23.1494218585, 39414.2000050930, 1.9936508E-05, 3.7830496E-07, -1.8037978E-09, 1.9336825, 9.4222182890, 33314.7656989005, 6.0925100E-04, 1.0016445E-06, -4.7453563E-09, 1.8707647, 20.8369929680, 30457.2066251194, -1.2971236E-04, 1.2727746E-07, -5.6301898E-10, -1.7529659, 0.4873576771, -8886.0057043583, -3.3771956E-04, -4.6884877E-07, 2.2183135E-09, -1.4371624, 7.0979974718, -695.8760698485, 5.9190587E-05, 7.4463212E-08, -4.0360254E-10, -1.3725701, 1.4552986550, -209.5494658307, 4.3266809E-04, 5.1072212E-07, -2.4131116E-09, 1.2618162, 7.5108957121, 16728.3705250656, 1.1655481E-04, 2.8300097E-07, -1.3951435E-09};

    private static final double M11[] = {1.6768000, -0.0431256817, 628.3019552485, -2.6638815E-06, 6.1639211E-10, -5.4439728E-11, 0.5164200, 11.2260974062, 6585.7609102104, -2.1583699E-04, -1.8708058E-07, 9.3204945E-10, 0.4138300, 13.5816519784, 14914.4523349355, -6.3524240E-05, 6.3330532E-08, -2.5428962E-10, 0.3711500, 5.5402729076, 7700.3894694766, 1.5497663E-04, 2.4979472E-07, -1.1318993E-09, 0.2756000, 2.3124288905, 8956.9933799736, 1.4964887E-04, 2.5102751E-07, -1.2407788E-09, 0.2459863, -25.6198212459, -2.3011998397, 1.5231275E-04, 2.5041111E-07, -1.1863391E-09, 0.0711800, 7.9982533891, 7842.3648207073, -2.2116475E-04, -1.8584780E-07, 8.2317000E-10, 0.0612800, 10.3538079614, 16171.0562454324, -6.8852003E-05, 6.4563317E-08, -3.6316908E-10};

    private static final double M12[] = {0.0048700, -0.0431256817, 628.3019552485, -2.6638815E-06, 6.1639211E-10, -5.4439728E-11, 0.0022800, -27.1705318325, -2.3011998397, 1.5231275E-04, 2.5041111E-07, -1.1863391E-09, 0.0015000, 11.2260974062, 6585.7609102104, -2.1583699E-04, -1.8708058E-07, 9.3204945E-10};

    private static final double M20[] = {18461.2400600, 1.6279052448, 8433.4661576405, -6.4021295E-05, -4.9499477E-09, 2.0216731E-11, 1010.1671484, 3.9834598170, 16762.1575823656, 8.8291456E-05, 2.4546117E-07, -1.1661223E-09, 999.6936555, 0.7276493275, -104.7747329154, 2.1633405E-04, 2.5536106E-07, -1.2065558E-09, 623.6524746, 8.7690283983, 7109.2881325435, -2.1668263E-06, 6.8896872E-08, -3.2894608E-10, 199.4837596, 9.6692843156, 15647.5290230993, -2.8252217E-04, -1.9141414E-07, 8.9782646E-10, 166.5741153, 6.4134738261, -1219.4032921817, -1.5447958E-04, -1.8151424E-07, 8.5739300E-10, 117.2606951, 12.0248388879, 23976.2204478244, -1.3020942E-04, 5.8996977E-08, -2.8851262E-10, 61.9119504, 6.3390143893, 25090.8490070907, 2.4060421E-04, 4.9587228E-07, -2.3524614E-09, 33.3572027, 11.1245829706, 15437.9795572686, 1.5014592E-04, 3.1930799E-07, -1.5152852E-09, 31.7596709, 3.0832038997, 8223.9166918098, 3.6864680E-04, 5.0577218E-07, -2.3928949E-09, 29.5766003, 8.8121540801, 6480.9861772950, 4.9705523E-07, 6.8280480E-08, -2.7450635E-10, 15.5662654, 4.0579192538, -9548.0947169068, -3.0679233E-04, -4.3192536E-07, 2.0437321E-09, 15.1215543, 14.3803934601, 32304.9118725496, 2.2103334E-05, 3.0940809E-07, -1.4748517E-09, -12.0941511, 8.7259027166, 7737.5900877920, -4.8307078E-06, 6.9513264E-08, -3.8338581E-10, 8.8681426, 9.7124099974, 15019.2270678508, -2.7985829E-04, -1.9203053E-07, 9.5226618E-10, 8.0450400, 0.6687636586, 8399.7091105030, -3.3191993E-05, 3.2017096E-08, -1.5363746E-10, 7.9585542, 12.0679645696, 23347.9184925760, -1.2754553E-04, 5.8380585E-08, -2.3407289E-10, 7.4345550, 6.4565995078, -1847.7052474301, -1.5181570E-04, -1.8213063E-07, 9.1183272E-10, -6.7314363, -4.0265854988, -16133.8556271171, -9.0955337E-05, -2.4484477E-07, 1.1116826E-09, 6.5795750, 16.8104074692, 14323.3509980023, -2.2066770E-04, -1.1756732E-07, 5.4866364E-10, -6.4600721, 1.5847795630, 9061.7681128890, -6.6685176E-05, -4.3335556E-09, -3.4222998E-11, -6.2964773, 4.8837157343, 25300.3984729215, -1.9206388E-04, -1.4849843E-08, 6.0650192E-11, -5.6323538, -0.7707750092, 733.0766881638, -2.1899793E-04, -2.5474467E-07, 1.1521161E-09, -5.3683961, 6.8263720663, 16204.8433027325, -9.7115356E-05, 2.7023515E-08, -1.3414795E-10, -5.3112784, 3.9403341353, 17390.4595376141, 8.5627574E-05, 2.4607756E-07, -1.2205621E-09, -5.0759179, 0.6845236457, 523.5272223331, 2.1367016E-04, 2.5597745E-07, -1.2609955E-09, -4.8396143, -1.6710309265, -7805.1642023920, 6.1357413E-05, 5.5663398E-09, -7.4656459E-11, -4.8057401, 3.5705615768, -662.0890125485, 3.0927234E-05, 3.6923410E-08, -1.7458141E-10, 3.9840545, 8.6945689615, 33419.5404318159, 3.9291696E-04, 7.4628340E-07, -3.5388005E-09, 3.6744619, 19.1659620415, 22652.0424227274, -6.8354947E-05, 1.3284380E-07, -6.3767543E-10, 2.9984815, 20.0662179587, 31190.2833132833, -3.4871029E-04, -1.2746721E-07, 5.8909710E-10, 2.7986413, -2.5281611620, -16971.7070481963, 3.4437664E-04, 2.6526096E-07, -1.2469893E-09, 2.4138774, 17.7106633865, 22861.5918885581, -5.0102304E-04, -3.7787833E-07, 1.7754362E-09, 2.1863132, 5.5132179088, -9757.6441827375, 1.2587576E-04, 7.8796768E-08, -3.6937954E-10, 2.1461692, 13.4801375428, 23766.6709819937, 3.0245868E-04, 5.6971910E-07, -2.7016242E-09, 1.7659832, 11.1677086523, 14809.6776020201, 1.5280981E-04, 3.1869159E-07, -1.4608454E-09, -1.6244212, 7.3137297434, 7318.8375983742, -4.3483492E-04, -4.4182525E-07, 2.0841655E-09, 1.5813036, 5.4387584720, 16552.6081165349, 5.2095955E-04, 7.5618329E-07, -3.5792340E-09, 1.5197528, 16.7359480324, 40633.6032972747, 1.7441609E-04, 5.5981921E-07, -2.6611908E-09, 1.5156341, 1.7023646816, -17876.7861416319, -4.5910508E-04, -6.8233647E-07, 3.2300712E-09, 1.5102092, 5.4977296450, 8399.6847301375, -3.3094061E-05, 3.1973462E-08, -1.5436468E-10, -1.3178223, 9.6261586339, 16275.8309783478, -2.8518605E-04, -1.9079775E-07, 8.4338673E-10, -1.2642739, 11.9817132061, 24604.5224030729, -1.3287330E-04, 5.9613369E-08, -3.4295235E-10, 1.1918723, 22.4217725310, 39518.9747380084, -1.9639754E-04, 1.2294390E-07, -5.9724197E-10, 1.1346110, 14.4235191419, 31676.6099173011, 2.4767216E-05, 3.0879170E-07, -1.4204120E-09, 1.0857810, 8.8552797618, 5852.6842220465, 3.1609367E-06, 6.7664088E-08, -2.2006663E-10, -1.0193852, 7.2392703065, 33629.0898976466, -3.9751134E-05, 2.3556127E-07, -1.1256889E-09, -0.8227141, 11.0814572888, 16066.2815125171, 1.4748204E-04, 3.1992438E-07, -1.5697249E-09, 0.8042238, 3.5274358950, -33.7870573000, 2.8263353E-05, 3.7539802E-08, -2.2902113E-10, 0.8025939, 6.7832463846, 16833.1452579809, -9.9779237E-05, 2.7639907E-08, -1.8858767E-10, -0.7931866, -6.3821400710, -24462.5470518423, -2.4326809E-04, -4.9525589E-07, 2.2980217E-09, -0.7910153, 6.3703481443, -591.1013369332, -1.5714346E-04, -1.8089785E-07, 8.0295327E-10, -0.6674056, 9.1819266386, 24533.5347274576, 5.5197395E-05, 2.7743463E-07, -1.3204870E-09, 0.6502226, 4.1010449356, -10176.3966721553, -3.0412845E-04, -4.3254175E-07, 2.0981718E-09, -0.6388131, 6.2958887075, 25719.1509623392, 2.3794032E-04, 4.9648867E-07, -2.4069012E-09};

    private static final double M21[] = {0.0743000, 11.9537467337, 6480.9861772950, 4.9705523E-07, 6.8280480E-08, -2.7450635E-10, 0.0304300, 8.7259027166, 7737.5900877920, -4.8307078E-06, 6.9513264E-08, -3.8338581E-10, 0.0222900, 12.8540026510, 15019.2270678508, -2.7985829E-04, -1.9203053E-07, 9.5226618E-10, 0.0199900, 15.2095572232, 23347.9184925760, -1.2754553E-04, 5.8380585E-08, -2.3407289E-10, 0.0186900, 9.5981921614, -1847.7052474301, -1.5181570E-04, -1.8213063E-07, 9.1183272E-10, 0.0169600, 7.1681781524, 16133.8556271171, 9.0955337E-05, 2.4484477E-07, -1.1116826E-09, 0.0162300, 1.5847795630, 9061.7681128890, -6.6685176E-05, -4.3335556E-09, -3.4222998E-11, 0.0141900, -0.7707750092, 733.0766881638, -2.1899793E-04, -2.5474467E-07, 1.1521161E-09};

    private static final double M30[] = {385000.5290396, 1.5707963268, 0.0000000000, 0.0000000E+00, 0.0000000E+00, 0.0000000E+00, -20905.3551378, 3.9263508990, 8328.6914247251, 1.5231275E-04, 2.5041111E-07, -1.1863391E-09, -3699.1109330, 9.6121753977, 7214.0628654588, -2.1850087E-04, -1.8646419E-07, 8.7760973E-10, -2955.9675626, 11.9677299699, 15542.7542901840, -6.6188121E-05, 6.3946925E-08, -3.0872935E-10, -569.9251264, 6.2819054713, 16657.3828494503, 3.0462550E-04, 5.0082223E-07, -2.3726782E-09, 246.1584797, 7.2566208254, -1114.6285592663, -3.7081362E-04, -4.3687530E-07, 2.0639488E-09, -204.5861179, 12.0108556517, 14914.4523349355, -6.3524240E-05, 6.3330532E-08, -2.5428962E-10, -170.7330791, 14.3232845422, 23871.4457149091, 8.6124629E-05, 3.1435804E-07, -1.4950684E-09, -152.1378118, 9.6553010794, 6585.7609102104, -2.1583699E-04, -1.8708058E-07, 9.3204945E-10, -129.6202242, -0.8278839272, -7700.3894694766, -1.5497663E-04, -2.4979472E-07, 1.1318993E-09, 108.7427014, 6.7692631483, 7771.3771450920, -3.3094061E-05, 3.1973462E-08, -1.5436468E-10, 104.7552944, 3.8832252173, 8956.9933799736, 1.4964887E-04, 2.5102751E-07, -1.2407788E-09, 79.6605685, 0.6705404095, -8538.2408905558, 2.8035534E-04, 2.6031101E-07, -1.2267725E-09, 48.8883284, 1.5276706450, 628.3019552485, -2.6638815E-06, 6.1639211E-10, -5.4439728E-11, -34.7825237, 20.0091090408, 22756.8171556428, -2.8468899E-04, -1.2251727E-07, 5.6888037E-10, 30.8238599, 11.9246042882, 16171.0562454324, -6.8852003E-05, 6.4563317E-08, -3.6316908E-10, 24.2084985, 9.5690497159, 7842.3648207073, -2.2116475E-04, -1.8584780E-07, 8.2317000E-10, -23.2104305, 8.6374600436, 24986.0742741754, 4.5693825E-04, 7.5123334E-07, -3.5590172E-09, -21.6363439, 17.6535544685, 14428.1257309177, -4.3700174E-04, -3.7292838E-07, 1.7552195E-09, -16.6747239, 6.7261374666, 8399.6791003405, -3.5757942E-05, 3.2589854E-08, -2.0880440E-10, 14.4026890, 4.9010662531, -9443.3199839914, -5.2312637E-04, -6.8728642E-07, 3.2502879E-09, -12.8314035, 14.3664102239, 23243.1437596606, 8.8788511E-05, 3.1374165E-07, -1.4406287E-09, -11.6499478, 22.3646636130, 31085.5085803679, -1.3237624E-04, 1.2789385E-07, -6.1745870E-10, -10.4447578, 16.6788391144, 32200.1371396342, 2.3843738E-04, 5.6476915E-07, -2.6814075E-09, 10.3211071, 8.7119194804, -1324.1780250970, 6.1854469E-05, 7.3846820E-08, -3.4916281E-10, 10.0562033, 7.2997465071, -1742.9305145148, -3.6814974E-04, -4.3749170E-07, 2.1183885E-09, -9.8844667, 12.0539813334, 14286.1503796870, -6.0860358E-05, 6.2714140E-08, -1.9984990E-10, 8.7515625, 6.3563649081, -9652.8694498221, -9.0458282E-05, -1.7656429E-07, 8.3717626E-10, -8.3791067, 4.4137085761, -557.3142796331, -1.8540681E-04, -2.1843765E-07, 1.0319744E-09, -7.0026961, -3.1834384995, -16029.0808942018, -3.0728938E-04, -5.0020584E-07, 2.3182384E-09, 6.3220032, 9.1248177206, 16100.0685698171, 1.1921869E-04, 2.8238458E-07, -1.3407038E-09, 5.7508579, 6.2387797896, 17285.6848046987, 3.0196162E-04, 5.0143862E-07, -2.4271179E-09, -4.9501349, 9.6984267611, 5957.4589549619, -2.1317311E-04, -1.8769697E-07, 9.8648918E-10, -4.4211770, 3.0260949818, -209.5494658307, 4.3266809E-04, 5.1072212E-07, -2.4131116E-09, 4.1311145, 11.0674740526, 7004.5133996281, 2.1416722E-04, 3.2425793E-07, -1.5355019E-09, -3.9579827, 20.0522347225, 22128.5152003943, -2.8202511E-04, -1.2313366E-07, 6.2332010E-10, 3.2582371, 14.8106422192, 14985.4400105508, -2.5159493E-04, -1.5449073E-07, 7.2324505E-10, -3.1483020, 4.8266068163, 16866.9323152810, -1.2804259E-04, -9.8998954E-09, 4.0433461E-11, 2.6164092, 14.2801588604, 24499.7476701576, 8.3460748E-05, 3.1497443E-07, -1.5495082E-09, 2.3536310, 9.5259240342, 8470.6667759558, -2.2382863E-04, -1.8523141E-07, 7.6873027E-10, -2.1171283, -0.8710096090, -7072.0875142282, -1.5764051E-04, -2.4917833E-07, 1.0774596E-09, -1.8970368, 17.6966801503, 13799.8237756692, -4.3433786E-04, -3.7354477E-07, 1.8096592E-09, -1.7385258, 2.0581540038, -8886.0057043583, -3.3771956E-04, -4.6884877E-07, 2.2183135E-09, -1.5713944, 22.4077892948, 30457.2066251194, -1.2971236E-04, 1.2727746E-07, -5.6301898E-10, -1.4225541, 24.7202181853, 39414.2000050930, 1.9936508E-05, 3.7830496E-07, -1.8037978E-09, -1.4189284, 17.1661967915, 23314.1314352759, -9.9282182E-05, 9.5920387E-08, -4.6309403E-10, 1.1655364, 3.8400995356, 9585.2953352221, 1.4698499E-04, 2.5164390E-07, -1.2952185E-09, -1.1169371, 10.9930146158, 33314.7656989005, 6.0925100E-04, 1.0016445E-06, -4.7453563E-09, 1.0656723, 1.4845449633, 1256.6039104970, -5.3277630E-06, 1.2327842E-09, -1.0887946E-10, 1.0586190, 11.9220903668, 8364.7398411275, -2.1850087E-04, -1.8646419E-07, 8.7760973E-10, -0.9333176, 9.0816920389, 16728.3705250656, 1.1655481E-04, 2.8300097E-07, -1.3951435E-09, 0.8624328, 12.4550876470, 6656.7485858257, -4.0390768E-04, -4.0490184E-07, 1.9095841E-09, 0.8512404, 4.3705828944, 70.9876756153, -1.8807069E-04, -2.1782126E-07, 9.7753467E-10, -0.8488018, 16.7219647962, 31571.8351843857, 2.4110126E-04, 5.6415276E-07, -2.6269678E-09, -0.7956264, 3.5134526588, -9095.5551701890, 9.4948529E-05, 4.1873358E-08, -1.9479814E-10};

    private static final double M31[] = {0.5139500, 12.0108556517, 14914.4523349355, -6.3524240E-05, 6.3330532E-08, -2.5428962E-10, 0.3824500, 9.6553010794, 6585.7609102104, -2.1583699E-04, -1.8708058E-07, 9.3204945E-10, 0.3265400, 3.9694765808, 7700.3894694766, 1.5497663E-04, 2.4979472E-07, -1.1318993E-09, 0.2639600, 0.7416325637, 8956.9933799736, 1.4964887E-04, 2.5102751E-07, -1.2407788E-09, 0.1230200, -1.6139220085, 628.3019552485, -2.6638815E-06, 6.1639211E-10, -5.4439728E-11, 0.0775400, 8.7830116346, 16171.0562454324, -6.8852003E-05, 6.4563317E-08, -3.6316908E-10, 0.0606800, 6.4274570623, 7842.3648207073, -2.2116475E-04, -1.8584780E-07, 8.2317000E-10, 0.0497000, 12.0539813334, 14286.1503796870, -6.0860358E-05, 6.2714140E-08, -1.9984990E-10};

    private static final double M1n[] = {3.81034392032, 8.39968473021E+03, -3.31919929753E-05, 3.20170955005E-08, -1.53637455544E-10};

    private static final double nutB[] = {2.1824391966, -33.757045954, 0.0000362262, 3.7340E-08, -2.8793E-10, -171996, -1742, 92025, 89, 3.5069406862, 1256.663930738, 0.0000105845, 6.9813E-10, -2.2815E-10, -13187, -16, 5736, -31, 1.3375032491, 16799.418221925, -0.0000511866, 6.4626E-08, -5.3543E-10, -2274, -2, 977, -5, 4.3648783932, -67.514091907, 0.0000724525, 7.4681E-08, -5.7586E-10, 2062, 2, -895, 5, 0.0431251803, -628.301955171, 0.0000026820, 6.5935E-10, 5.5705E-11, -1426, 34, 54, -1, 2.3555557435, 8328.691425719, 0.0001545547, 2.5033E-07, -1.1863E-09, 712, 1, -7, 0, 3.4638155059, 1884.965885909, 0.0000079025, 3.8785E-11, -2.8386E-10, -517, 12, 224, -6, 5.4382493597, 16833.175267879, -0.0000874129, 2.7285E-08, -2.4750E-10, -386, -4, 200, 0, 3.6930589926, 25128.109647645, 0.0001033681, 3.1496E-07, -1.7218E-09, -301, 0, 129, -1, 3.5500658664, 628.361975567, 0.0000132664, 1.3575E-09, -1.7245E-10, 217, -5, -95, 3};

    private static final double GXC_e[] = {0.016708634, -0.000042037, -0.0000001267};

    private static final double rad = 180 * 3600 / Math.PI;

    private static final double RAD = 180 / Math.PI;

    private static final double J2000 = 2451545;

    private static final double preceB[] = {0, 50287.92262, 111.24406, 0.07699, -0.23479, -0.00178, 0.00018, 0.00001};

    private static final double GXC_p[] = {102.93735 / RAD, 1.71946 / RAD, 0.00046 / RAD};

    private static final double GXC_l[] = {280.4664567 / RAD, 36000.76982779 / RAD, 0.0003032028 / RAD, 1 / 49931000 / RAD, -1 / 153000000 / RAD};

    private static final double GXC_k = 20.49552 / rad;

    private static final double[] dts = {-4000, 108371.7, -13036.80, 392.000, 0.0000, -500, 17201.0, -627.82, 16.170, -0.3413, -150, 12200.6, -346.41, 5.403, -0.1593, 150, 9113.8, -328.13, -1.647, 0.0377, 500, 5707.5, -391.41, 0.915, 0.3145, 900, 2203.4, -283.45, 13.034, -0.1778, 1300, 490.1, -57.35, 2.085, -0.0072, 1600, 120.0, -9.81, -1.532, 0.1403, 1700, 10.2, -0.91, 0.510, -0.0370, 1800, 13.4, -0.72, 0.202, -0.0193, 1830, 7.8, -1.81, 0.416, -0.0247, 1860, 8.3, -0.13, -0.406, 0.0292, 1880, -5.4, 0.32, -0.183, 0.0173, 1900, -2.3, 2.06, 0.169, -0.0135, 1920, 21.2, 1.69, -0.304, 0.0167, 1940, 24.2, 1.22, -0.064, 0.0031, 1960, 33.2, 0.51, 0.231, -0.0109, 1980, 51.0, 1.29, -0.026, 0.0032, 2000, 64.7, -1.66, 5.224, -0.2905, 2150, 279.4, 732.95, 429.579, 0.0158, 6000};

    private double EnnT = 0;
    private double MnnT = 0;

    private double D = 1;

    private class ZD {
        double Lon;
        double Obl;
    }

    /**
     * 生成某年农历二十四节气数组数据
     * Create solar term by year.
     *
     * @param year 某年
     * @return ...
     */
    String[][] buildSolarTerm(int year) {
        String[] tmp = new String[24];
        double jd = 365.2422 * (year - 2000), q;
        int j;
        for (int i = 0; i < tmp.length; i++) {
            j = (i - 5);
            q = angleCal(jd + j * 15.2, j * 15, 0);
            q = q + J2000 + (double) 8 / 24;
            setFromJD(q, true);
            tmp[i] = String.valueOf((int) D);
        }
        return DataUtils.arraysConvert(tmp, 12, 2);
    }

    private void setFromJD(double jd, boolean UTC) {
        if (UTC)
            jd -= this.deltaT2(jd - J2000);
        jd += 0.5;

        double A = int2(jd);
        double D;

        if (A > 2299161) {
            D = int2((A - 1867216.25) / 36524.25);
            A += 1 + D - int2(D / 4);
        }
        A += 1524;
        double y = int2((A - 122.1) / 365.25);
        D = A - int2(365.25 * y);
        double m = int2(D / 30.6001);
        this.D = D - int2(m * 30.6001);
        y -= 4716;
        m--;
        if (m > 12)
            m -= 12;
        if (m <= 2)
            //noinspection UnusedAssignment
            y++;
    }

    private double int2(double v) {
        v = Math.floor(v);
        if (v < 0)
            return v + 1;
        return v;
    }

    private double deltaT2(double jd) {
        return this.deltaT(jd / 365.2425 + 2000) / 86400.0;
    }

    private double deltaT(double y) {
        int i;
        for (i = 0; i < 100; i += 5)
            if (y < dts[i + 5] || i == 95)
                break;
        double t1 = (y - dts[i]) / (dts[i + 5] - dts[i]) * 10;
        double t2 = t1 * t1;
        double t3 = t2 * t1;
        return dts[i + 1] + dts[i + 2] * t1 + dts[i + 3] * t2 + dts[i + 4] * t3;
    }

    private double angleCal(double t1, double jiao, int lx) {
        double t2 = t1, t = 0, v;
        if (lx == 0)
            t2 += 360;
        else
            t2 += 25;
        jiao *= Math.PI / 180;
        double v1 = jiaoCai(lx, t1, jiao);
        double v2 = jiaoCai(lx, t2, jiao);
        if (v1 < v2)
            v2 -= 2 * Math.PI;
        double k = 1, k2;
        for (int i = 0; i < 10; i++) {
            k2 = (v2 - v1) / (t2 - t1);
            if (Math.abs(k2) > 1e-15)
                k = k2;
            t = t1 - v1 / k;
            v = jiaoCai(lx, t, jiao);
            if (v > 1)
                v -= 2 * Math.PI;
            if (Math.abs(v) < 1e-8)
                break;
            t1 = t2;
            v1 = v2;
            t2 = t;
            v2 = v;
        }
        return t;
    }

    private double jiaoCai(int lx, double t, double jiao) {
        double[] sun = earCal(t);
        sun[0] += Math.PI;
        sun[1] = -sun[1];
        addGxc(t, sun);
        if (lx == 0) {
            ZD d = nutation(t);
            sun[0] += d.Lon;
            return rad2mrad(jiao - sun[0]);
        }
        double[] moon = moonCal(t);
        return rad2mrad(jiao - (moon[0] - sun[0]));
    }

    private double[] moonCal(double jd) {
        MnnT = jd / 36525;
        double t1 = MnnT, t2 = t1 * t1, t3 = t2 * t1, t4 = t3 * t1;
        double[] llr = new double[3];
        llr[0] = (Mnn(M10) + Mnn(M11) * t1 + Mnn(M12) * t2) / rad;
        llr[1] = (Mnn(M20) + Mnn(M21) * t1) / rad;
        llr[2] = (Mnn(M30) + Mnn(M31) * t1) * 0.999999949827;
        llr[0] = llr[0] + M1n[0] + M1n[1] * t1 + M1n[2] * t2 + M1n[3] * t3 + M1n[4] * t4;
        llr[0] = rad2mrad(llr[0]);
        addPrece(jd, llr);
        return llr;
    }

    private void addPrece(double jd, double[] zb) {
        int i;
        double t = 1, v = 0, t1 = jd / 365250;
        for (i = 1; i < 8; i++) {
            t *= t1;
            v += preceB[i] * t;
        }
        zb[0] = rad2mrad(zb[0] + (v + 2.9965 * t1) / rad);
    }

    private double Mnn(double[] F) {
        double v = 0, t1 = MnnT, t2 = t1 * t1, t3 = t2 * t1, t4 = t3 * t1;
        for (int i = 0; i < F.length; i += 6)
            v += F[i] * Math.sin(F[i + 1] + t1 * F[i + 2] + t2 * F[i + 3] + t3 * F[i + 4] + t4 * F[i + 5]);
        return v;
    }

    private ZD nutation(double t) {
        ZD d = new ZD();
        d.Lon = 0;
        d.Obl = 0;
        t /= 36525;
        double c, t1 = t, t2 = t1 * t1, t3 = t2 * t1, t4 = t3 * t1;
        for (int i = 0; i < nutB.length; i += 9) {
            c = nutB[i] + nutB[i + 1] * t1 + nutB[i + 2] * t2 + nutB[i + 3] * t3 + nutB[i + 4] * t4;
            d.Lon += (nutB[i + 5] + nutB[i + 6] * t / 10) * Math.sin(c);
            d.Obl += (nutB[i + 7] + nutB[i + 8] * t / 10) * Math.cos(c);
        }
        d.Lon /= rad * 10000;
        d.Obl /= rad * 10000;
        return d;
    }

    private void addGxc(double t, double[] zb) {
        double t1 = t / 36525;
        double t2 = t1 * t1;
        double t3 = t2 * t1;
        double t4 = t3 * t1;
        double L = GXC_l[0] + GXC_l[1] * t1 + GXC_l[2] * t2 + GXC_l[3] * t3 + GXC_l[4] * t4;
        double p = GXC_p[0] + GXC_p[1] * t1 + GXC_p[2] * t2;
        double e = GXC_e[0] + GXC_e[1] * t1 + GXC_e[2] * t2;
        double dL = L - zb[0], dP = p - zb[0];
        zb[0] -= GXC_k * (Math.cos(dL) - e * Math.cos(dP)) / Math.cos(zb[1]);
        zb[1] -= GXC_k * Math.sin(zb[1]) * (Math.sin(dL) - e * Math.sin(dP));
        zb[0] = rad2mrad(zb[0]);
    }

    private double[] earCal(double jd) {
        EnnT = jd / 365250;
        double llr[] = new double[3];
        double t1 = EnnT, t2 = t1 * t1, t3 = t2 * t1, t4 = t3 * t1, t5 = t4 * t1;
        llr[0] = Enn(E10) + Enn(E11) * t1 + Enn(E12) * t2 + Enn(E13) * t3 + Enn(E14) * t4 + Enn(E15) * t5;
        llr[1] = Enn(E20) + Enn(E21) * t1;
        llr[2] = Enn(E30) + Enn(E31) * t1 + Enn(E32) * t2 + Enn(E33) * t3;
        llr[0] = rad2mrad(llr[0]);
        return llr;
    }

    private double rad2mrad(double v) {
        v = v % (2 * Math.PI);
        if (v < 0)
            return v + 2 * Math.PI;
        return v;
    }

    private double Enn(double[] F) {
        double v = 0;
        for (int i = 0; i < F.length; i += 3)
            v += F[i] * Math.cos(F[i + 1] + EnnT * F[i + 2]);
        return v;
    }
}
