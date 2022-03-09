package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class demoController {
    char[][] board = {{'n', 'n', 'n'}, {'n', 'n', 'n'}, {'n', 'n', 'n'}};
    char turn = 'x';
    boolean winner = false;

    @GetMapping("/")
    String homePage(Model model) {
        model.addAttribute("board", board);
        model.addAttribute("turn", turn);
        model.addAttribute("win", winner);
        return "demo";
    }

    @GetMapping("/{i}/{j}")
    String changeboard(@PathVariable Integer i, @PathVariable Integer j, Model model) {
        board[i][j] = turn;

        if (turn == 'x') {
            turn = 'o';
        } else {
            turn = 'x';
        }

        model.addAttribute("board", board);
        model.addAttribute("turn", turn);
        model.addAttribute("win", winner);
        return "redirect:/check";
    }

    @GetMapping("/check")
    String checkforwin(Model model) {
        char[] winnerBoard = new char[3];
        char[][] win = {{'x', 'x', 'x'}, {'o', 'o', 'o'}};
        for (int i = 0; i < board.length; i++) {
            if (Arrays.equals(board[i], win[0]) || Arrays.equals(board[i], win[1])) {
                winner = true;
            }
        }
        for (int i = 0; i < board.length; i++) {
            winnerBoard[i] = board[i][0];
        }
        if (Arrays.equals(winnerBoard, win[0]) || Arrays.equals(winnerBoard, win[1])) winner = true;

        for (int i = 0; i < board.length; i++) {
            winnerBoard[i] = board[i][1];

        }
        if (Arrays.equals(winnerBoard, win[0]) || Arrays.equals(winnerBoard, win[1])) winner = true;
        for (int i = 0; i < board.length; i++) {
            winnerBoard[i] = board[i][2];
        }
        if (Arrays.equals(winnerBoard, win[0]) || Arrays.equals(winnerBoard, win[1])) winner = true;
        for (int i = 0; i < board.length; i++) {
            winnerBoard[i] = board[i][i];
        }
        if (Arrays.equals(winnerBoard, win[0]) || Arrays.equals(winnerBoard, win[1])) winner = true;
        for (int i = 0, j = 2; i < 3 && j >= 0; i++, j--) {
            winnerBoard[i] = board[i][j];

        }
        if (Arrays.equals(winnerBoard, win[0]) || Arrays.equals(winnerBoard, win[1])) winner = true;
        model.addAttribute("board", board);
        model.addAttribute("turn", turn);
        model.addAttribute("win", winner);
        return "redirect:/";
    }

    @GetMapping("/new")
    String newGame(Model model) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = 'n';
            }
        }
        turn = 'x';
        winner = false;
        model.addAttribute("board", board);
        model.addAttribute("turn", turn);
        model.addAttribute("win", winner);
        return "redirect:/";
    }

    @GetMapping("/ai")
    String test(Model model) {
        double val = minimax(this.board, this.turn);
        int[] location = locateMini(val, this.board, this.turn);
        return "redirect:/"+location[0]+"/"+location[1];
    }

    double minimax(char[][] b, char t) {
        int moves = 0;
        Double value;
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                if (b[i][j] == 'n') moves++;
            }
            if (moves == 0) {
                return evaluateBoard(b);
            }
        }
        if (t == 'x') {
            value = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[i].length; j++) {
                    if (b[i][j] == 'n') {
                        b[i][j] = 'x';
                        value = Math.max(value, minimax(b, 'o'));
                        b[i][j] = 'n';
                    }
                }


            }
        } else {
            value = Double.POSITIVE_INFINITY;
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[i].length; j++) {
                    if (b[i][j] == 'n') {
                        b[i][j] = 'o';
                        value = Math.min(value, minimax(b, 'x'));
                        b[i][j] = 'n';
                    }
                }
            }
        }
        return value;
    }

    double evaluateBoard(char[][] b) {
        char[] winnerBoard = new char[3];
        char[][] win = {{'x', 'x', 'x'}, {'o', 'o', 'o'}};
        for (int i = 0; i < board.length; i++) {
            if (Arrays.equals(board[i], win[0])) return 1.0;
            if (Arrays.equals(board[i], win[1])) return -1.0;
        }
        for (int i = 0; i < board.length; i++) {
            winnerBoard[i] = board[i][0];
        }
        if (Arrays.equals(winnerBoard, win[0])) return 1.0;
        if (Arrays.equals(winnerBoard, win[1])) return -1.0;
        for (int i = 0; i < board.length; i++) {
            winnerBoard[i] = board[i][1];

        }
        if (Arrays.equals(winnerBoard, win[0])) return 1.0;
        if (Arrays.equals(winnerBoard, win[1])) return -1.0;
        for (int i = 0; i < board.length; i++) {
            winnerBoard[i] = board[i][2];
        }
        if (Arrays.equals(winnerBoard, win[0])) return 1.0;
        if (Arrays.equals(winnerBoard, win[1])) return -1.0;
        for (int i = 0; i < board.length; i++) {
            winnerBoard[i] = board[i][i];
        }
        if (Arrays.equals(winnerBoard, win[0])) return 1.0;
        if (Arrays.equals(winnerBoard, win[1])) return -1.0;
        for (int i = 0, j = 2; i < 3 && j >= 0; i++, j--) {
            winnerBoard[i] = board[i][j];

        }
        if (Arrays.equals(winnerBoard, win[0])) return 1.0;
        if (Arrays.equals(winnerBoard, win[1])) return -1.0;
        return 0.0;
    }

    int[] locateMini(double v, char[][] b, char t) {
        Double value;
        int[] location = new int[2];
        if (t == 'x') {
            value = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[i].length; j++) {
                    if (b[i][j] == 'n') {
                        b[i][j] = 'x';
                        value = Math.max(value, minimax(b, 'o'));
                        b[i][j] = 'n';
                        if (v == value) {
                            location[0] = i;
                            location[1] = j;
                        }
                    }
                }
            }


        } else {
            value = Double.POSITIVE_INFINITY;
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[i].length; j++) {
                    if (b[i][j] == 'n') {
                        b[i][j] = 'o';
                        value = Math.min(value, minimax(b, 'x'));
                        b[i][j] = 'n';
                        if (v == value) {
                            location[0] = i;
                            location[1] = j;
                        }
                    }
                }
            }
        }
        return location;
    }
}
