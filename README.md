# CSE 321: Analysis and Design of Algorithms - Programming Assignment 01
This GitHub repository contains the source code and documentation for the first Programming Assignment related to the academic course `CSE 321: Analysis and Design of Algorithms`, offered from the department of Computer and Systems Engineering in Fall 2023. 
The assignment consists of two main tasks:

## Task 01: Kth Element Selection
In this task, we are required to implement and compare the performance of three different methods used to select the kth smallest element of an array. The three methods are:
1. The randomized divide-and-conquer approach [CLRS 9.2].
2. The deterministic linear-time selection algorithm using median-of-medians [CLRS 9.3].
3. The naive method using sorting and returning the k-th smallest number (not required for implementation).

For this assignment, we will implement the first two methods and provide an analysis of their performance. The performance analysis should involve running the implemented methods on input arrays of various sizes (up to 10^7 elements), with the arrays being randomly generated. For each data point, we should compute the average of multiple runs to obtain accurate performance measurements.

---

## Task 02: Maximum Side Length of Squares
In the second task, we are required to design and implement a program that calculates the maximum side length of a square that can be drawn around each given two-dimensional point. The squares should not intersect, except at their sides. The key details for this task include:
- Each point will be at the center of the square around it.
- The sides of the squares can only be horizontal and vertical.

---