{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-parts.url = "github:hercules-ci/flake-parts";
  };

  outputs =
    {
      self,
      nixpkgs,
      flake-parts,
    }@inputs:
    flake-parts.lib.mkFlake { inherit inputs; } (
      { ... }:
      {
        systems = [
          "x86_64-linux"
        ];
        perSystem =
          {
            pkgs,
            lib,
            self',
            ...
          }:
          {
            devShells.default = pkgs.mkShell {
              packages = with pkgs; [
                jdk25
                gradle_9
              ];
            };
          };
      }
    );
}
