## Project name
End-to-end UI automation framework for testing the core functionality of – [igram.world](https://igram.world)

## Project description
The project validates media downloading workflows, navigation behavior, link health, and input error handling using Selenium WebDriver and TestNG.
This automation framework verifies both positive and negative user flows of the igram.world website.

## The framework:
- validates downloading videos, photos, Reels and IGTV
- verifies navigation across the website
- detects broken links across multiple pages
- generates execution reports
- captures screenshots on failures
- input validation and error handling
- redirect verification
- multi-window handling
- HTML reporting

_The project follows Page Object Model (POM) and uses TestNG for test execution._
_Test data for positive and negative scenarios is externalized using JSON files for scalability and maintainability._

## Test Coverage
### Positive Scenarios
1. Main Page:
- Page loads successfully
- Validate input field behavior
- Download videos
- Download photos
- Download Reels
- Download IGTV
- Navigation across main sections

 2. Link Validation:
- Collect all <a> links on a page
- Filter invalid URLs
- Send HTTP requests to validate status codes
- Detect and report broken links (4xx / 5xx)
- Generate detailed failure logs

### Negative & Error Validation Scenarios
1. Input Validation:
- Invalid or unsupported links
- Private Instagram account links
- Empty input submission
- Incomplete URLs

2. Error Handling Verification:
- Validate correct error messages displayed to user
- Verify redirection (Extension / Desktop versions)
- Validate new browser window handling
- Confirm required field validation behavior
- Ensure correct redirect URLs

## Framework Architecture
The framework is built using:
- Page Object Model (POM)
- Reusable BaseTest setup
- Custom utility classes (e.g. LinkValidator)
- TestNG Listeners for screenshot capture
- ExtentReports for HTML reporting
- Data-driven testing using JSON files

## Tech stack
Java | Selenium | TestNG | Maven | ExtentReports | Apache Commons IO | Jackson (JSON parsing)

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
- screenshots on failure

## Screenshots
Location:
reports/testCaseName.png

## CI/CD Integration
The project is integrated with Jenkins for automated test execution.
