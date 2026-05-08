# XML Parser Report

## Optimization Summary

The XML parser was optimized from approximately 100–140 seconds to approximately 12–20 seconds.

## Optimizations

1. Replaced repeated subqueries with preloaded hash sets/maps for existing movies and stars.
2. Used batch inserts with autocommit disabled, inserting 500 records per batch.
3. Used SAX parsing instead of DOM parsing to avoid loading entire XML files into memory.

## Final Runtime

Approximate final parser time: 12–20 seconds.

## Inconsistency Report

| Category | Count |
|---|---:|
| Stars inserted | 6,838 |
| Duplicate star names skipped | 24 |
| Genres inserted | 104 |
| Movies inserted | 7,457 |
| Duplicate movies | 27 |
| Inconsistent movies | 305 |
| Movies with no stars | 4,326 |
| Movies with no genres | 3,216 |
| Genres in movies inserted | 7,186 |
| Inconsistent genres not inserted | 2,650 |
| Stars in movies inserted | 31,305 |
| Movies not found for stars-in-movies | 1,566 |
| Stars not found for stars-in-movies | 15,816 |
| Duplicate stars in movies | 251 |
