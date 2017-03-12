package Congruence.Solver;

import Congruence.Model.CongruenceSystem;

import java.math.BigInteger;

@FunctionalInterface
public interface CongruenceSystemSolver {
    BigInteger solve(CongruenceSystem system);
}
