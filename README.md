# Pre-work - *ThisTodo*

ThisTodo is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Robert Lee**

Time spent: **22** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [ ] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Ability to delete multiple items at once.
* [x] Persist todo items using a NoSql based persistence mechanism
* [x] Change UX flow for creating a new item, via FAB and BottomDialog
* [x] Ability to swipe to mark todo items complete or unfinished
* [x] UI updates to launcher icon, startup screen, and empty view

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/qjufa8k.gif' title='Video Walkthrough' width='600' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** The Android app development platform provides many resources available to help aid in development. There are lot of helpful discussions in StackOverflow
and many open source libraries provide solutions to common problems developers could face in Android. Android's approach to layouts and user interfaces is designed in mind to scale
with the many different device hardware specifications and form factors in the world that can run Android. From low resolution, large screen sizes hardware to TV, phone form factors
, the Android platform provides ways to support these devices.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** An adapter in this context is used as a way to map data to the rows of a `ListView`. In Android the `ArrayAdapter` provides a mechanism for data to be rendered in a row's view and manages the list's data.
The adapter is important since it provides the logic to populating the visible rows in the list--providing handling for when a row's view needs updating. The `convertView` is the `View` of the row inflated from a specified
layout. The `convertView`'s purpose is render the row's UI with populated date such as text.

## Notes

Describe any challenges encountered while building the app.

## License

    Copyright 2017 Robert Lee

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.