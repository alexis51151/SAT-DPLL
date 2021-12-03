<div id="top"></div>



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<h3 align="center">The SAT S'Owl'ver</h3>

  <p align="center">
    In the context of Prof. Vardi <a href="https://www.cs.rice.edu/~vardi/comp409/">COMP 509 Logic class </a> at <a href="https://www.rice.edu/">Rice University</a>, I have developed a DPLL SAT Solver in Java that I hope you will enjoy!
    <br />
    <br />
  </p>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

The DPLL SAT Solver is based on an interative implementation of the DPLL design and currently supports 3 heuristics for the Splitting rule:
* Random Choice
* Two Clauses
* Jeroslow-Wang


<p align="right">(<a href="#top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

* Java 17


<!-- USAGE EXAMPLES -->
## Usage
Here is an example of usage to run the solver with the `RandomChoice` heuristics over a DIMACS-formatted file `test.cnf`:

```java -jar Solver.jar test.cnf RandomChoice```

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

Alexis Le Glaunec - alexis.leglaunec@rice.edu

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[license-shield]: https://img.shields.io/github/license/alexis51151/SAT-DPLL.svg?style=for-the-badge
[license-url]: https://github.com/alexis51151/SAT-DPLL/blob/master/LICENSE.md
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/alexis-leglaunec
[product-screenshot]: images/screenshot.png
