{}:

let
  sources = import ./npins;
  pkgs = import sources.nixpkgs {};
in
  pkgs.mkShell {
    buildInputs = with pkgs; [
      temurin-bin-21
      gradle
      git
    ];

    JAVA_HOME = "${pkgs.temurin-bin-21}";
  }
