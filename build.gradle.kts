plugins {
    base
    id("test-report-aggregation")
    id("jacoco-report-aggregation")
}

repositories {
    mavenCentral()
}

dependencies {
    testReportAggregation(project(":xyzwps-bean"))
    testReportAggregation(project(":xyzwps-bedrock"))
    testReportAggregation(project(":xyzwps-dollar"))
    testReportAggregation(project(":xyzwps-json"))

    jacocoAggregation(project(":xyzwps-bean"))
    jacocoAggregation(project(":xyzwps-bedrock"))
    jacocoAggregation(project(":xyzwps-dollar"))
    jacocoAggregation(project(":xyzwps-json"))
}

reporting {
    reports {
        val testAggregateTestReport by creating(AggregateTestReport::class) {
            testType = TestSuiteType.UNIT_TEST
        }
    }
    reports {
        val testCodeCoverageReport by creating(JacocoCoverageReport::class) {
            testType = TestSuiteType.UNIT_TEST
        }
    }
}

tasks.check {
    dependsOn(tasks.named<TestReport>("testAggregateTestReport"))
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
}

