# TMDb Channel Mapper

This project is designed to process and manage JSON files generated from TMDb daily dumps. The main goal is to map the files, fetch missing image information for channels, and save the data in a structured and optimized format. It also handles various tasks like filtering, formatting, minifying, and sorting the JSON files for better data management and processing.

## Functions Overview

### `readAndFilterJson`
Filters out channels that do not have any associated images (`logo_path`, `backdrop_path`, or `poster_path`).
- **Input:** JSON file containing a list of channels.
- **Output:** Filtered JSON file with only channels that have at least one image.
- **Purpose:** Ensure that the output JSON contains only relevant channels with images.

### `formatJsonFile`
Formats a JSON file by ensuring that lines are comma-separated and wrapped in a list.
- **Input:** Raw JSON file with each object on a separate line.
- **Output:** Properly formatted JSON file.
- **Purpose:** Prepare JSON files that may have been exported in a raw, unformatted structure for proper parsing.

### `readAndMinifyJson`
Minifies a JSON file by removing unnecessary whitespaces while preserving the data structure.
- **Input:** JSON file containing a list of channels.
- **Output:** Minified JSON file.
- **Purpose:** Reduce file size for efficient storage and transmission.

### `fetchChannelImages`
Fetches missing image data for channels using TMDb API.
- **Input:** JSON file containing a list of channels.
- **Output:** Updated JSON file with fetched image data.
- **Purpose:** Retrieve missing `logo_path`, `backdrop_path`, or `poster_path` for channels from the TMDb API.
- **Implementation Details:**
    - Uses a semaphore to limit concurrent API requests to 50 requests per second, adhering to TMDb's rate limit.
    - Processes channels concurrently using coroutines to optimize performance.

### `readAndSeparateJson`
Splits a large JSON file into smaller parts.
- **Input:** JSON file and the number of divisions.
- **Output:** Multiple JSON files, each containing a portion of the original data.
- **Purpose:** Manage large datasets by dividing them into smaller, more manageable chunks.

### `readAndSortJson`
Sorts channels based on the presence of images and their IDs.
- **Input:** JSON file containing a list of channels.
- **Output:** Sorted JSON file.
- **Purpose:** Organize data to prioritize channels with images.

## Why Semaphores?
The `fetchChannelImages` function uses a semaphore to limit the number of concurrent API requests to TMDb. This ensures compliance with the TMDb API rate limit of 50 requests per second while maximizing throughput. By using coroutines and a semaphore, the application efficiently fetches missing data without exceeding the API's limits or risking throttling.

## Project Motivation
This project was developed to process the daily dump files provided by TMDb and enhance the dataset by fetching and saving image information for channels. By automating the mapping and fetching processes, this tool ensures that the data is complete, well-structured, and ready for further use in applications or analytics.

## How to Use
1. Place the input JSON file in the desired location.
2. Call the respective function for the task you want to perform (e.g., filtering, formatting, fetching images).
3. Provide the input and output file paths as parameters to the function.
4. The processed file will be saved in the specified output location.

