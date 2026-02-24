## Project name
UI Automation – [igram.world](https://igram.world)

## Project description
This automation project tests the main functionality of the [igram.world](https://igram.world) website.

## The framework:
- validates downloading videos, photos, Reels and IGTV
- verifies navigation across the website
- detects broken links across multiple pages
- generates execution reports
- captures screenshots on failures

_The project follows Page Object Model (POM) and uses TestNG for test execution._

## Test Scenarios
Main Page:
- Page loads successfully
- Validate input field behavior
- Download videos
- Download photos
- Download Reels
- Download IGTV
- Navigation across main sections

## Link Validation:
- Collect all links on a page
- Filter invalid URLs
- Send HTTP requests
- Validate response status codes
- Report broken links

## Tech stack
Java | Selenium | TestNG | Maven | ExtentReports

## Framework Features
- Page Object Model design
- TestNG test structure
- Reusable base test setup
- Custom utilities for link validation
- Screenshot capture on failures
- HTML reporting with ExtentReports
- External test data using JSON

## How to Run Tests
1. Clone the repository
` git clone https://github.com/SofiaBell181/InstagramDownloader.git `
2. Navigate to the project `cd InstagramDownloader`
3. Run tests `mvn test` Or run the suite directly: testng.xml

## Reports
Test report is generated using ExtentReports in:
/reports/index.html

## The report includes:
- passed tests
- failed tests
- stack traces
- screenshots

## Screenshots
Location:
reports/testCaseName.png

## Future improvements
