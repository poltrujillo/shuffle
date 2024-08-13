<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
<!-- FUTURES
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]-->





<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/LaSalleGracia-Projectes/projecte-aplicaci-web-servidor-tim-berners-lee/tree/readMe">
    <img src="images/light-full-size.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Shuffle</h3>

  <p align="center">
    project_subtitle
    <br />
    <!-- Link to Memory PDF -->
    <a href="https://github.com/LaSalleGracia-Projectes/projecte-aplicaci-web-servidor-tim-berners-lee"><strong>Explora »</strong></a>
    <br />
    <br />
    <!-- Link to Demo Video -->
    <a href="https://github.com/LaSalleGracia-Projectes/projecte-aplicaci-web-client-tim-berners-lee">Web App</a>
    ·
    <a href="https://github.com/LaSalleGracia-Projectes/projecte-aplicaci-web-servidor-tim-berners-lee">Android App</a>
    ·
    <a href="https://github.com/LaSalleGracia-Projectes/projecte-aplicaci-extra-tim-berners-lee">Servidor C#</a>
  </p>
</div>
<!-- TABLE OF CONTENTS -->
<details>
  <summary>Tabla de Contentido</summary>
  <ol>
    <li>
      <a href="#about-the-project">Sobre el Proyecto</a>
      <ul>
        <li><a href="#built-with">Construido Con</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Empezando</a>
      <ul>
        <li><a href="#prerequisites">Prerequisitos</a></li>
        <li><a href="#installation">Instalacion</a></li>
        <li><a href="#deployment">Despliegue</a></li>
      </ul>
    </li>
    <li><a href="#usage">Uso</a></li>
    <li><a href="#license">Licencia</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## Sobre el Proyecto

[![Product Name Screen Shot][product-screenshot]]

Servidor Progrmado en Laravel para administrar nuestra base de datos de usuario que utlizan Shuffle


<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contribuidores
[![Contributors]][contributors-url]

* Pol Trujillo Albert  - poltrujillo7@gmail.com
* Ramon Mormeneo Ferrer - [@twitter_handle](https://twitter.com/twitter_handle) - ramonmormeneo@gmail.com
* Carla Flores Macià - carlaflores2204@gmail.com

### Construido Con

* [![Laravel][Laravel.com]][Laravel-url]

<!-- See: https://github.com/alexandresanlim/Badges4-README.md-Profile?tab=readme-ov-file#-terminal -->

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Empezando

Para usar esta applicacion necesitamos tener lo sigueinte:Un compilador de codigo como Visual Studio Code para poder iniciar el servidor y una app como XAMP para poder crear nuestra base de datos Mysql

### Prerequisitos

Requirements for the software and other tools to build, test and push 
- [Visual Studio Code](https://code.visualstudio.com)
- [MySQL](https://www.mysql.com/downloads/)


### Instalacion

1. Crear directory
   ```sh
   mkdir project-dir
   ```
2. Clonar el repo
   ```sh
   git clone https://github.com/LaSalleGracia-Projectes/projecte-aplicaci-web-servidor-tim-berners-lee
   ```
3. Abrir el repo con visual studio y abrir la terminal

4. Instalar compose
   ```sh
   php install composer
   ```

5. Copiar el conteindo de .env.example en .env

### Despliegue
1. Encender el XAMP y activar tanto Apache como MySQL

2. Si es la primera ejeccucion, entrar a phpadmin y creara una base de datos, el nombre de esta base de datos tiene que coincidir con en nombre que esta en el fichero .env del proyecto laravel (shuffle)

3. Abrir la terminal desde Visual Studio Code

3. Ejecutar esta comanda para crear la base de datos
   ```sh
   php artisan migrate --seed
   ```

4. Ejecutar esta comanda para activar el servidor
   ```sh
   php artisan serve --host=”IP del pc”
   ```


<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Uso

Este programa es para ser ejecutado en un ordenador que despues sera usado por nuestras applicaciones, cuando se este ejecutando, el resto de applicaciones tendran acceso a la base de datos. 

<p align="right">(<a href="#readme-top">back to top</a>)</p>






<!-- LICENSE -->
## Licencia

[![MIT License]][license-url]

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>






<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/LaSalleGracia-Projectes/projecte-aplicaci-web-servidor-tim-berners-lee/LICENSE.txt
[contributors-url]: https://github.com/LaSalleGracia-Projectes/projecte-aplicaci-web-servidor-tim-berners-lee/network/dependencies
[product-screenshot]: images/pngegg.png
[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Vue.js]: https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D
[Vue-url]: https://vuejs.org/
[Laravel.com]: https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white
[Laravel-url]: https://laravel.com
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[Visual-url]: https://developer.android.com/studio
[Visual.com]: https://img.shields.io/badge/AndroidStudio-4598d3?style=for-the-badge&logo=android&logoColor=A4C639
[Spoty-url]: https://open.spotify.com/intl-es
[Spoty.com]: https://img.shields.io/badge/Spotify-1DB954?style=for-the-badge&logo=spotify&logoColor=black