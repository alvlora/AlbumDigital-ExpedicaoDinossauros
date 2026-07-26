# -lbum-Digital-Expedi-o-Dinossauros
Este projeto consiste em um álbum de figurinhas digital com temática de dinossauros.
É um projeto criado para faculdade em conjunto com um colega, feito para aperfeiçoar nossos conhecimentos até então.
O sistema permite que colecionadores criem uma conta, acessem seu álbum e expandam sua coleção por meio de um sistema de escavação.

Colecionador

Para adicionar novos dinossauros ao álbum, o usuário deve acessar a área de Escavação, onde, a cada 24 horas, são disponibilizadas 3 figurinhas aleatórias. Cada figurinha possui um código exclusivo, que pode ser utilizado para adicioná-la permanentemente à coleção do colecionador.

Autor

O usuário com perfil de Autor é responsável pelo gerenciamento do conteúdo do álbum. Ele pode alterar a capa do álbum, adicionar novas figurinhas, editar as figurinhas existentes e excluir aquelas que desejar.

Administrador

O Administrador (ADM) é o responsável pelo gerenciamento dos usuários do sistema. Apenas esse perfil possui permissão para criar novos usuários, podendo definir o nível de acesso de cada um deles como Colecionador, Autor ou outro Administrador.

Usuários Cadastrados:

Login: Alvaro / Senha: 123      (ADMINISTRADOR)
Login: Jonny  / Senha: 123    	(ADMINISTRADOR)
Login: autor  / Senha: 123456    (AUTOR)
Login: ranger / Senha: 123456   (COLECIONADOR)

O projeto foi configurado para exigir apenas a instalação do Java no computador. 
Não é necessário instalar o Maven separadamente, pois a aplicação utiliza o Maven Wrapper, que já acompanha o projeto.

Para executar a aplicação, basta abrir o projeto em um terminal na pasta raiz e utilizar o comando:

./mvnw spring-boot:run

Após a inicialização, a aplicação estará disponível no navegador através do endereço configurado para o servidor local (localhost).
